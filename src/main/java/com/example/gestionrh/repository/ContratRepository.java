package com.example.gestionrh.repository;

import com.example.gestionrh.model.ContratEmploye;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class ContratRepository {

    public List<ContratEmploye> findByEmploye(Long employeId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM ContratEmploye c WHERE c.employe.id = :employeId ORDER BY c.dateDebut DESC", ContratEmploye.class)
                    .setParameter("employeId", employeId)
                    .getResultList();
        }
    }

    public List<ContratEmploye> findCDDExpirantDans30Jours() {
        LocalDate now = LocalDate.now();
        LocalDate in30Days = now.plusDays(30);
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM ContratEmploye c WHERE c.typeContrat = 'CDD' AND c.dateFin BETWEEN :now AND :in30Days", ContratEmploye.class)
                    .setParameter("now", now)
                    .setParameter("in30Days", in30Days)
                    .getResultList();
        }
    }

    public void save(ContratEmploye contrat) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(contrat);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
