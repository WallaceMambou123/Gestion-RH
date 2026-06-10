package com.example.gestionrh.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.service.AuthService;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/InscriptionPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String mdp = request.getParameter("mdp");
        String roleStr = request.getParameter("role");

        try {
            Utilisateur newUser = Utilisateur.builder()
                    .nom(nom)
                    .prenom(prenom)
                    .username(username)
                    .email(email)
                    .password(mdp)
                    .role(Utilisateur.Role.valueOf(roleStr))
                    .actif(true)
                    .build();

            authService.register(newUser);
            response.sendRedirect(request.getContextPath() + "/connexion?registered=1");
        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de l'inscription : " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/InscriptionPage.jsp").forward(request, response);
        }
    }
}