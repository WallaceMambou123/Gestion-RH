package com.example.gestionrh.service;

import com.example.gestionrh.model.ContratEmploye;
import com.example.gestionrh.model.Conge;
import com.example.gestionrh.repository.ContratRepository;
import com.example.gestionrh.repository.EmployeRepository;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiqueService {

    private final ContratRepository contratRepository = new ContratRepository();
    private final EmployeRepository employeRepository = new EmployeRepository();

    public Map<String, BigDecimal> getMasseSalarialeParDepartement() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> results = em.createQuery(
                "SELECT d.nom, SUM(e.salaireBase) FROM Employe e JOIN e.departement d GROUP BY d.nom ORDER BY d.nom",
                Object[].class).getResultList();
            Map<String, BigDecimal> stats = new HashMap<>();
            for (Object[] res : results) {
                stats.put((String) res[0], (BigDecimal) res[1]);
            }
            return stats;
        }
    }

    public BigDecimal getMasseSalarialeTotale() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            BigDecimal total = em.createQuery(
                "SELECT SUM(e.salaireBase) FROM Employe e", BigDecimal.class).getSingleResult();
            return total != null ? total : BigDecimal.ZERO;
        }
    }

    public double getTauxAbsenteisme() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long maladieDays = em.createQuery(
                "SELECT SUM(c.nbJours) FROM Conge c WHERE c.typeConge = :type AND c.statut = :statut",
                Long.class)
                .setParameter("type", Conge.TypeConge.MALADIE)
                .setParameter("statut", Conge.StatutConge.APPROUVE)
                .getSingleResult();
            Long totalEmployes = em.createQuery("SELECT COUNT(e) FROM Employe e", Long.class)
                .getSingleResult();
            if (totalEmployes == null || totalEmployes == 0) return 0.0;
            long joursOuvresTheoriques = totalEmployes * 220;
            double taux = (maladieDays == null ? 0 : maladieDays.doubleValue() / joursOuvresTheoriques) * 100;
            return Math.round(taux * 100.0) / 100.0;
        }
    }

    public Map<String, Long> getRepartitionParTypeContrat() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> results = em.createQuery(
                "SELECT e.typeContrat, COUNT(e) FROM Employe e GROUP BY e.typeContrat",
                Object[].class).getResultList();
            Map<String, Long> stats = new HashMap<>();
            for (Object[] res : results) {
                stats.put(res[0] != null ? res[0].toString() : "INCONNU", (Long) res[1]);
            }
            return stats;
        }
    }

    public Map<String, Long> getRepartitionParDepartement() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> results = em.createQuery(
                "SELECT d.nom, COUNT(e) FROM Departement d LEFT JOIN d.employes e GROUP BY d.nom ORDER BY d.nom",
                Object[].class).getResultList();
            Map<String, Long> stats = new HashMap<>();
            for (Object[] res : results) {
                stats.put((String) res[0], (Long) res[1]);
            }
            return stats;
        }
    }

    public List<ContratEmploye> getAlertesCDDExpirants() {
        return contratRepository.findCDDExpirantDans30Jours();
    }

    public long countEmployes() {
        return employeRepository.count();
    }
}
