package com.example.gestionrh.service;

import com.example.gestionrh.model.Conge;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.repository.CongeRepository;
import com.example.gestionrh.repository.EmployeRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CongeService {
    private final CongeRepository congeRepository = new CongeRepository();
    private final EmployeRepository employeRepository = new EmployeRepository();

    public void demanderConge(Conge conge) {
        long days = ChronoUnit.DAYS.between(conge.getDateDebut(), conge.getDateFin()) + 1;
        conge.setNbJours((int) days);
        conge.setStatut(Conge.StatutConge.DEMANDE);
        congeRepository.save(conge);
    }

    public void approuverConge(Long congeId, String responsable) {
        Conge conge = congeRepository.findById(congeId);
        if (conge == null || conge.getStatut() != Conge.StatutConge.DEMANDE) return;

        conge.setStatut(Conge.StatutConge.APPROUVE);
        conge.setApprouvePar(responsable);

        // Décrémenter le solde uniquement pour les congés annuels
        if (conge.getTypeConge() == Conge.TypeConge.ANNUEL) {
            Employe e = conge.getEmploye();
            int solde = (e.getSoldeCongesJours() != null ? e.getSoldeCongesJours() : 0);
            e.setSoldeCongesJours(Math.max(0, solde - conge.getNbJours()));
            employeRepository.save(e);
        }
        congeRepository.save(conge);
    }

    public void refuserConge(Long congeId, String motifRefus, String responsable) {
        Conge conge = congeRepository.findById(congeId);
        if (conge == null) return;
        conge.setStatut(Conge.StatutConge.REFUSE);
        String motifActuel = conge.getMotif() != null ? conge.getMotif() : "";
        if (motifRefus != null && !motifRefus.isBlank()) {
            conge.setMotif(motifActuel + " [REFUS: " + motifRefus + "]");
        }
        conge.setApprouvePar(responsable);
        congeRepository.save(conge);
    }

    public List<Conge> listerTous() {
        return congeRepository.findAll();
    }

    public List<Conge> listerParStatut(Conge.StatutConge statut) {
        return congeRepository.findByStatut(statut);
    }

    public List<Conge> listerParEmploye(Long employeId) {
        return congeRepository.findByEmploye(employeId);
    }

    public List<Conge> listerParDepartement(Long deptId) {
        return congeRepository.findByDepartement(deptId);
    }

    public List<Conge> listerEnAttente() {
        return congeRepository.findEnAttente();
    }

    public long countEnAttente() {
        return congeRepository.countByStatut(Conge.StatutConge.DEMANDE);
    }

    public double calculerTauxAbsenteisme(int annee) {
        // Approche: SUM(jours_maladie_approuvés) / (nb_employes * 220) * 100
        try {
            Long totalEmployes = employeRepository.count();
            if (totalEmployes == 0) return 0.0;
            List<Conge> congesMAladie = congeRepository.findByStatut(Conge.StatutConge.APPROUVE);
            int joursMaladie = congesMAladie.stream()
                .filter(c -> c.getTypeConge() == Conge.TypeConge.MALADIE
                          && c.getDateDebut() != null
                          && c.getDateDebut().getYear() == annee)
                .mapToInt(Conge::getNbJours)
                .sum();
            double joursOuvres = totalEmployes * 220.0;
            return Math.round((joursMaladie / joursOuvres) * 100 * 100.0) / 100.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
