package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.UtilisateurRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/validation")
public class ValidationServlet extends HttpServlet {
    private final UtilisateurRepository utilisateurRepository = new UtilisateurRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null || user.getRole() != Utilisateur.Role.RH) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        List<Utilisateur> allUsers = utilisateurRepository.findAll();
        List<Utilisateur> pendingUsers = allUsers.stream()
                .filter(u -> !u.isActif())
                .collect(Collectors.toList());

        request.setAttribute("pendingUsers", pendingUsers);
        request.setAttribute("page", "validation");
        request.getRequestDispatcher("/WEB-INF/views/admin/validation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null || user.getRole() != Utilisateur.Role.RH) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        Long userId = Long.parseLong(request.getParameter("userId"));

        Utilisateur target = utilisateurRepository.findById(userId);
        if (target != null) {
            if ("approuver".equals(action)) {
                target.setActif(true);
                utilisateurRepository.save(target);
            } else if ("rejeter".equals(action)) {
//                utilisateurRepository.delete(userId);
            }
        }

        response.sendRedirect(request.getContextPath() + "/validation?success=1");
    }
}
