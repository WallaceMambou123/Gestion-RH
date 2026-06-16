package com.example.gestionrh.repository;

import com.example.gestionrh.model.FichePaie;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FichePaieRepository {

    public FichePaie findById(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(FichePaie.class, id);
        }
    }

    public List<FichePaie> findByEmploye(Long employeId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT f FROM FichePaie f JOIN FETCH f.employe WHERE f.employe.id = :employeId ORDER BY f.mois DESC",
                FichePaie.class)
                .setParameter("employeId", employeId)
                .getResultList();
        }
    }

    public Optional<FichePaie> findByEmployeAndMois(Long employeId, String mois) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            FichePaie fp = em.createQuery(
                "SELECT f FROM FichePaie f JOIN FETCH f.employe WHERE f.employe.id = :employeId AND f.mois = :mois",
                FichePaie.class)
                .setParameter("employeId", employeId)
                .setParameter("mois", mois)
                .getSingleResult();
            return Optional.of(fp);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<FichePaie> findByMois(String mois) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT f FROM FichePaie f JOIN FETCH f.employe e JOIN FETCH e.departement WHERE f.mois = :mois ORDER BY e.nom",
                FichePaie.class)
                .setParameter("mois", mois)
                .getResultList();
        }
    }

    public List<FichePaie> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT f FROM FichePaie f JOIN FETCH f.employe ORDER BY f.mois DESC",
                FichePaie.class)
                .getResultList();
        }
    }

    public boolean existsByEmployeAndMois(Long employeId, String mois) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long count = em.createQuery(
                "SELECT COUNT(f) FROM FichePaie f WHERE f.employe.id = :employeId AND f.mois = :mois",
                Long.class)
                .setParameter("employeId", employeId)
                .setParameter("mois", mois)
                .getSingleResult();
            return count != null && count > 0;
        }
    }

    public Map<String, BigDecimal> sumSalaireNetByDepartement() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> results = em.createQuery(
                "SELECT d.nom, SUM(f.salaireNet) FROM FichePaie f " +
                "JOIN f.employe e JOIN e.departement d GROUP BY d.nom ORDER BY d.nom",
                Object[].class)
                .getResultList();
            Map<String, BigDecimal> stats = new HashMap<>();
            for (Object[] row : results) {
                stats.put((String) row[0], (BigDecimal) row[1]);
            }
            return stats;
        }
    }

    public void save(FichePaie fichePaie) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (fichePaie.getId() == null) {
                em.persist(fichePaie);
            } else {
                em.merge(fichePaie);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
