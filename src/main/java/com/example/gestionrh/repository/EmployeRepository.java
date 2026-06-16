package com.example.gestionrh.repository;

import com.example.gestionrh.model.Employe;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EmployeRepository {

    public List<Employe> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                    "SELECT e FROM Employe e LEFT JOIN FETCH e.departement ORDER BY e.nom",
                    Employe.class).getResultList();
        }
    }

    public List<Employe> findAllPaginated(int page, int size) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT e FROM Employe e LEFT JOIN FETCH e.departement ORDER BY e.nom",
                            Employe.class)
                    .setFirstResult((page - 1) * size)
                    .setMaxResults(size)
                    .getResultList();
        }
    }

    public List<Employe> findByDepartement(Long deptId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT e FROM Employe e WHERE e.departement.id = :deptId ORDER BY e.nom",
                Employe.class)
                .setParameter("deptId", deptId)
                .getResultList();
        }
    }

    public List<Employe> findByNomOrPrenom(String query) {
        if (query == null || query.isBlank()) return Collections.emptyList();
        String pattern = "%" + query.toLowerCase() + "%";
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT e FROM Employe e WHERE LOWER(e.nom) LIKE :p OR LOWER(e.prenom) LIKE :p ORDER BY e.nom",
                Employe.class)
                .setParameter("p", pattern)
                .getResultList();
        }
    }

    public Optional<Employe> findByMatricule(String matricule) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Employe e = em.createQuery(
                "SELECT e FROM Employe e WHERE e.matricule = :matricule",
                Employe.class)
                .setParameter("matricule", matricule)
                .getSingleResult();
            return Optional.of(e);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Employe> findByEmail(String email) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Employe e = em.createQuery(
                "SELECT e FROM Employe e WHERE e.email = :email",
                Employe.class)
                .setParameter("email", email)
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
            Employe e = em.createQuery(
                "SELECT e FROM Employe e WHERE e.utilisateur.id = :userId",
                Employe.class)
                .setParameter("userId", userId)
                .getSingleResult();
            return Optional.of(e);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public long count() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT COUNT(e) FROM Employe e", Long.class).getSingleResult();
        }
    }

    public long countByDepartement(Long deptId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                "SELECT COUNT(e) FROM Employe e WHERE e.departement.id = :deptId",
                Long.class)
                .setParameter("deptId", deptId)
                .getSingleResult();
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

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Employe employe = em.find(Employe.class, id);
            if (employe != null) em.remove(employe);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
