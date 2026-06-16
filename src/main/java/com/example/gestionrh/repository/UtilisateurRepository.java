package com.example.gestionrh.repository;

import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class UtilisateurRepository {

    public Optional<Utilisateur> findByUsername(String username) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Utilisateur user = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.username = :username",
                Utilisateur.class)
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Utilisateur> findByEmail(String email) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Utilisateur user = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.email = :email",
                Utilisateur.class)
                .setParameter("email", email)
                .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Utilisateur findById(Long id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.find(Utilisateur.class, id);
        }
    }

    public List<Utilisateur> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT u FROM Utilisateur u ORDER BY u.nom", Utilisateur.class)
                .getResultList();
        }
    }

    public boolean existsByUsername(String username) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long count = em.createQuery(
                "SELECT COUNT(u) FROM Utilisateur u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
            return count != null && count > 0;
        }
    }

    public boolean existsByEmail(String email) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long count = em.createQuery(
                "SELECT COUNT(u) FROM Utilisateur u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
            return count != null && count > 0;
        }
    }
    public long countPending() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT COUNT(u) FROM Utilisateur u WHERE u.actif = false", Long.class)
                    .getSingleResult();
        }
    }

    public void save(Utilisateur utilisateur) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (utilisateur.getId() == null) {
                em.persist(utilisateur);
            } else {
                em.merge(utilisateur);
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
            Utilisateur user = em.find(Utilisateur.class, id);
            if (user != null) em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
