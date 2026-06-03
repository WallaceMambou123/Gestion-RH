package com.example.gestionrh.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.gestionrh.service.AuthService;
import com.example.gestionrh.model.Utilisateur;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/connexion")
public class ConnexionServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/ConnexionPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("mdp");

        Optional<Utilisateur> userOpt = authService.login(username, password);

        if (userOpt.isPresent()) {
            HttpSession session = request.getSession();
            session.setAttribute("utilisateur", userOpt.get());
            session.setAttribute("role", userOpt.get().getRole().name());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Identifiants incorrects ou compte inactif.");
            request.getRequestDispatcher("/WEB-INF/views/ConnexionPage.jsp").forward(request, response);
        }
    }
}
