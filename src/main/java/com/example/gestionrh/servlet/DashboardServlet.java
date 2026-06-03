package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.service.StatistiqueService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final StatistiqueService statistiqueService = new StatistiqueService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        // Chargement des statistiques pour le dashboard (RH et Manager)
        if (user.getRole() != Utilisateur.Role.EMPLOYE) {
            request.setAttribute("masseSalariale", statistiqueService.getMasseSalarialeParDepartement());
            request.setAttribute("tauxAbsenteisme", statistiqueService.getTauxAbsenteisme());
            request.setAttribute("repartitionContrats", statistiqueService.getRepartitionParTypeContrat());
        }

        // Orientation vers la vue spécifique selon le rôle
        String role = user.getRole().name().toLowerCase();
        request.getRequestDispatcher("/WEB-INF/views/dashboard/dashboard-" + role + ".jsp").forward(request, response);
    }
}
