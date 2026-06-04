package com.example.gestionrh.repository;

import com.example.gestionrh.model.ContratEmploye;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class ContratRepository {

    public List<ContratEmploye> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM ContratEmploye c JOIN FETCH c.employe ORDER BY c.dateDebut DESC",
                ContratEmploye.class)
                .getResultList();
        }
    }

    public List<ContratEmploye> findByEmploye(Long employeId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM ContratEmploye c JOIN FETCH c.employe WHERE c.employe.id = :employeId ORDER BY c.dateDebut DESC",
                ContratEmploye.class)
                .setParameter("employeId", employeId)
                .getResultList();
        }
    }

    public ContratEmploye findById(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(ContratEmploye.class, id);
        }
    }

    public List<ContratEmploye> findCDDExpirantDans30Jours() {
        LocalDate now = LocalDate.now();
        LocalDate in30Days = now.plusDays(30);
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM ContratEmploye c JOIN FETCH c.employe " +
                "WHERE c.typeContrat = :type AND c.dateFin BETWEEN :now AND :in30Days " +
                "ORDER BY c.dateFin ASC",
                ContratEmploye.class)
                .setParameter("type", ContratEmploye.TypeContrat.CDD)
                .setParameter("now", now)
                .setParameter("in30Days", in30Days)
                .getResultList();
        }
    }

    public List<ContratEmploye> findCDDExpirantAvant(LocalDate date) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT c FROM ContratEmploye c JOIN FETCH c.employe " +
                "WHERE c.typeContrat = :type AND c.dateFin <= :date ORDER BY c.dateFin ASC",
                ContratEmploye.class)
                .setParameter("type", ContratEmploye.TypeContrat.CDD)
                .setParameter("date", date)
                .getResultList();
        }
    }

    public void save(ContratEmploye contrat) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (contrat.getId() == null) {
                em.persist(contrat);
            } else {
                em.merge(contrat);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            ContratEmploye c = em.find(ContratEmploye.class, id);
            if (c != null) em.remove(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
