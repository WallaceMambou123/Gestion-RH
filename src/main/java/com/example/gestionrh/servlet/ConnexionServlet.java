package com.example.gestionrh.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.gestionrh.service.AuthService;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.EmployeRepository;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {
    private final AuthService       authService      = new AuthService();
    private final EmployeRepository employeRepository = new EmployeRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si déjà connecté, rediriger vers dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("utilisateur") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        // Message de succès inscription
        String registered = request.getParameter("registered");
        if ("1".equals(registered)) {
            request.setAttribute("success", "Compte créé avec succès. Connectez-vous.");
        }
        request.getRequestDispatcher("/WEB-INF/views/ConnexionPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("mdp");

        Optional<Utilisateur> userOpt = authService.login(username, password);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            HttpSession session = request.getSession(true);
            session.setAttribute("utilisateur", user);
            session.setAttribute("role",        user.getRole().name());

            // Pré-charger l'employeId en session pour les employés
            if (user.getRole() == Utilisateur.Role.EMPLOYE) {
                employeRepository.findByUtilisateurId(user.getId()).ifPresent(emp -> {
                    session.setAttribute("employeId", emp.getId());
                });
            }

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Identifiants incorrects ou compte inactif.");
            request.getRequestDispatcher("/WEB-INF/views/ConnexionPage.jsp").forward(request, response);
        }
    }
}
