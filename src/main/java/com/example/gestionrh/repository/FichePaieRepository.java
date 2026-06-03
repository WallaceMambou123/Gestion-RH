package com.example.gestionrh.repository;

import com.example.gestionrh.model.FichePaie;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class FichePaieRepository {

    public Optional<FichePaie> findByEmployeAndMois(Long employeId, String mois) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            FichePaie fp = em.createQuery("SELECT f FROM FichePaie f WHERE f.employe.id = :employeId AND f.mois = :mois", FichePaie.class)
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
            return em.createQuery("SELECT f FROM FichePaie f WHERE f.mois = :mois", FichePaie.class)
                    .setParameter("mois", mois)
                    .getResultList();
        }
    }

    public void save(FichePaie fichePaie) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(fichePaie);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
