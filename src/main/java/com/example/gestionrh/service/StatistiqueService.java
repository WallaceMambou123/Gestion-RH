package com.example.gestionrh.service;

import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiqueService {

    public Map<String, BigDecimal> getMasseSalarialeParDepartement() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> results = em.createQuery(
                "SELECT d.nom, SUM(f.salaireNet) FROM FichePaie f JOIN f.employe e JOIN e.departement d GROUP BY d.nom", 
                Object[].class).getResultList();
            
            Map<String, BigDecimal> stats = new HashMap<>();
            for (Object[] res : results) {
                stats.put((String) res[0], (BigDecimal) res[1]);
            }
            return stats;
        }
    }

    public double getTauxAbsenteisme() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            // (SUM(jours_conge_maladie) / jours_ouvrés) * 100
            // Pour le TP, on simule jours_ouvrés = nb_employes * 20 jours
            Long maldieDays = em.createQuery(
                "SELECT SUM(c.nbJours) FROM Conge c WHERE c.typeConge = com.example.gestionrh.model.Conge.TypeConge.MALADIE AND c.statut = com.example.gestionrh.model.Conge.StatutConge.APPROUVE", 
                Long.class).getSingleResult();
            
            Long totalEmployes = em.createQuery("SELECT COUNT(e) FROM Employe e", Long.class).getSingleResult();
            
            if (totalEmployes == 0) return 0;
            
            long joursOuvresTheoriques = totalEmployes * 20; 
            return (maldieDays == null ? 0 : maldieDays.doubleValue() / joursOuvresTheoriques) * 100;
        }
    }

    public Map<String, Long> getRepartitionParTypeContrat() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> results = em.createQuery("SELECT e.typeContrat, COUNT(e) FROM Employe e GROUP BY e.typeContrat", Object[].class).getResultList();
            Map<String, Long> stats = new HashMap<>();
            for (Object[] res : results) {
                stats.put(res[0].toString(), (Long) res[1]);
            }
            return stats;
        }
    }
}
