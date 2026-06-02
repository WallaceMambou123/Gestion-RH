package com.example.gestionrh.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Afficher la page d'inscription
        request.getRequestDispatcher("/WEB-INF/views/InscriptionPage.jsp")
                .forward(request, response);
    }


}

/*
*  @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Récupération des champs du formulaire
        String nom      = request.getParameter("nom");
        String prenom   = request.getParameter("prenom");
        String username = request.getParameter("username");
        String email    = request.getParameter("email");
        String role     = request.getParameter("role");
        String mdp      = request.getParameter("mdp");
        String mdp2     = request.getParameter("mdp2");

        // ===== VALIDATION =====
        if (nom == null || nom.trim().isEmpty() ||
                prenom == null || prenom.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                mdp == null || mdp.trim().isEmpty()) {

            request.setAttribute("error", "Veuillez remplir tous les champs obligatoires.");
            request.getRequestDispatcher("/WEB-INF/views/InscriptionPage.jsp")
                    .forward(request, response);
            return;
        }

        if (!mdp.equals(mdp2)) {
            request.setAttribute("error", "Les mots de passe ne correspondent pas.");
            request.getRequestDispatcher("/WEB-INF/views/InscriptionPage.jsp")
                    .forward(request, response);
            return;
        }

        if (mdp.length() < 6) {
            request.setAttribute("error", "Le mot de passe doit contenir au moins 6 caractères.");
            request.getRequestDispatcher("/WEB-INF/views/InscriptionPage.jsp")
                    .forward(request, response);
            return;
        }

        // ===== HACHAGE DU MOT DE PASSE AVEC BCRYPT =====
        // TODO : ajouter jBCrypt dans pom.xml
        // String hashedPassword = BCrypt.hashpw(mdp, BCrypt.gensalt());

        // ===== SAUVEGARDE EN BASE =====
        // TODO : appeler UtilisateurDAO.save(nom, prenom, username, email, role, hashedPassword)

        System.out.println("Nouvel utilisateur : " + username + " / " + email + " / rôle : " + role);

        // ===== REDIRECTION VERS CONNEXION avec message succès =====
        response.sendRedirect(request.getContextPath() + "/connexion?registered=1");
    }
* */