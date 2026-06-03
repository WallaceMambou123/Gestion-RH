package com.example.gestionrh.service;

import com.example.gestionrh.model.Conge;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.repository.CongeRepository;
import com.example.gestionrh.repository.EmployeRepository;
import java.time.temporal.ChronoUnit;

public class CongeService {
    private final CongeRepository congeRepository = new CongeRepository();
    private final EmployeRepository employeRepository = new EmployeRepository();

    public void demanderConge(Conge conge) {
        // Calcul automatique du nombre de jours
        long days = ChronoUnit.DAYS.between(conge.getDateDebut(), conge.getDateFin()) + 1;
        conge.setNbJours((int) days);
        conge.setStatut(Conge.StatutConge.DEMANDE);
        congeRepository.save(conge);
    }

    public void approuverConge(Long congeId, String responsable) {
        Conge conge = congeRepository.findById(congeId);
        if (conge != null && conge.getStatut() == Conge.StatutConge.DEMANDE) {
            conge.setStatut(Conge.StatutConge.APPROUVE);
            conge.setApprouvePar(responsable);
            
            // Décrémenter le solde de congés de l'employé
            Employe e = conge.getEmploye();
            int nouveauSolde = (e.getSoldeCongesJours() != null ? e.getSoldeCongesJours() : 0) - conge.getNbJours();
            e.setSoldeCongesJours(Math.max(0, nouveauSolde));
            
            employeRepository.save(e);
            congeRepository.save(conge);
        }
    }

    public void refuserConge(Long congeId, String motif, String responsable) {
        Conge conge = congeRepository.findById(congeId);
        if (conge != null) {
            conge.setStatut(Conge.StatutConge.REFUSE);
            conge.setMotif(conge.getMotif() + " [REFUS: " + motif + "]");
            conge.setApprouvePar(responsable);
            congeRepository.save(conge);
        }
    }
}
