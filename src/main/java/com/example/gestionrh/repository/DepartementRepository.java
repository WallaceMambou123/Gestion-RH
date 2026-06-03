package com.example.gestionrh.repository;

import com.example.gestionrh.model.Departement;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class DepartementRepository {

    public List<Departement> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT d FROM Departement d", Departement.class).getResultList();
        }
    }

    public Departement findById(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(Departement.class, id);
        }
    }

    public void save(Departement departement) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (departement.getId() == null) {
                em.persist(departement);
            } else {
                em.merge(departement);
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
            Departement d = em.find(Departement.class, id);
            if (d != null) em.remove(d);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
