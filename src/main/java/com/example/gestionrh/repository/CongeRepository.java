package com.example.gestionrh.repository;

import com.example.gestionrh.model.Conge;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CongeRepository {

    public List<Conge> findByEmploye(Long employeId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM Conge c WHERE c.employe.id = :employeId", Conge.class)
                    .setParameter("employeId", employeId)
                    .getResultList();
        }
    }

    public List<Conge> findByStatut(Conge.StatutConge statut) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM Conge c WHERE c.statut = :statut", Conge.class)
                    .setParameter("statut", statut)
                    .getResultList();
        }
    }

    public Conge findById(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(Conge.class, id);
        }
    }

    public void save(Conge conge) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (conge.getId() == null) {
                em.persist(conge);
            } else {
                em.merge(conge);
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
