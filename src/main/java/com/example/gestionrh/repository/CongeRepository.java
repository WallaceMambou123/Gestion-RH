package com.example.gestionrh.repository;

import com.example.gestionrh.model.Conge;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CongeRepository {

    public List<Conge> findByEmploye(Long employeId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM Conge c JOIN FETCH c.employe WHERE c.employe.id = :employeId ORDER BY c.dateDebut DESC",
                Conge.class)
                .setParameter("employeId", employeId)
                .getResultList();
        }
    }

    public List<Conge> findByStatut(Conge.StatutConge statut) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM Conge c JOIN FETCH c.employe WHERE c.statut = :statut ORDER BY c.dateDebut DESC",
                Conge.class)
                .setParameter("statut", statut)
                .getResultList();
        }
    }

    public List<Conge> findEnAttente() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM Conge c JOIN FETCH c.employe WHERE c.statut = 'DEMANDE' ORDER BY c.dateDebut ASC",
                Conge.class)
                .getResultList();
        }
    }

    public List<Conge> findByDepartement(Long deptId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM Conge c JOIN FETCH c.employe e WHERE e.departement.id = :deptId ORDER BY c.dateDebut DESC",
                Conge.class)
                .setParameter("deptId", deptId)
                .getResultList();
        }
    }

    public List<Conge> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM Conge c JOIN FETCH c.employe ORDER BY c.dateDebut DESC",
                Conge.class)
                .getResultList();
        }
    }

    public Conge findById(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(Conge.class, id);
        }
    }

    public int countJoursMaladieByEmploye(Long employeId, int annee) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long result = em.createQuery(
                "SELECT SUM(c.nbJours) FROM Conge c " +
                "WHERE c.employe.id = :employeId " +
                "AND c.typeConge = :type " +
                "AND c.statut = :statut " +
                "AND YEAR(c.dateDebut) = :annee",
                Long.class)
                .setParameter("employeId", employeId)
                .setParameter("type", Conge.TypeConge.MALADIE)
                .setParameter("statut", Conge.StatutConge.APPROUVE)
                .setParameter("annee", annee)
                .getSingleResult();
            return result == null ? 0 : result.intValue();
        }
    }

    public long countByStatut(Conge.StatutConge statut) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long result = em.createQuery(
                "SELECT COUNT(c) FROM Conge c WHERE c.statut = :statut", Long.class)
                .setParameter("statut", statut)
                .getSingleResult();
            return result == null ? 0 : result;
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
