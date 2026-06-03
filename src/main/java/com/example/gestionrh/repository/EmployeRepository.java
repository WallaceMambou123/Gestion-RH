package com.example.gestionrh.repository;

import com.example.gestionrh.model.Employe;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class EmployeRepository {

    public List<Employe> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT e FROM Employe e FETCH JOIN e.departement", Employe.class).getResultList();
        }
    }

    public List<Employe> findByDepartement(Long deptId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT e FROM Employe e WHERE e.departement.id = :deptId", Employe.class)
                    .setParameter("deptId", deptId)
                    .getResultList();
        }
    }

    public Optional<Employe> findByMatricule(String matricule) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Employe e = em.createQuery("SELECT e FROM Employe e WHERE e.matricule = :matricule", Employe.class)
                    .setParameter("matricule", matricule)
                    .getSingleResult();
            return Optional.of(e);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Employe findById(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(Employe.class, id);
        }
    }

    public Optional<Employe> findByUtilisateurId(Long userId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Employe e = em.createQuery("SELECT e FROM Employe e WHERE e.utilisateur.id = :userId", Employe.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(e);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void save(Employe employe) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (employe.getId() == null) {
                em.persist(employe);
            } else {
                em.merge(employe);
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
