package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.service.CongeService;
import com.example.gestionrh.service.StatistiqueService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/stats")
public class StatistiqueServlet extends HttpServlet {
    private final StatistiqueService statistiqueService = new StatistiqueService();
    private final CongeService congeService = new CongeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;

        if (user == null || user.getRole() != Utilisateur.Role.RH) {
            response.sendRedirect(request.getContextPath() + "/403");
            return;
        }

        request.setAttribute("masseSalariale",    statistiqueService.getMasseSalarialeParDepartement());
        request.setAttribute("masseSalarialeTotal", statistiqueService.getMasseSalarialeTotale());
        request.setAttribute("repartitionContrats", statistiqueService.getRepartitionParTypeContrat());
        request.setAttribute("repartitionDepts",  statistiqueService.getRepartitionParDepartement());
        request.setAttribute("tauxAbsenteisme",   statistiqueService.getTauxAbsenteisme());
        request.setAttribute("alertesCDD",        statistiqueService.getAlertesCDDExpirants());
        request.setAttribute("effectifTotal",     statistiqueService.countEmployes());
        request.setAttribute("anneeActuelle",     LocalDate.now().getYear());

        request.getRequestDispatcher("/WEB-INF/views/stats/index.jsp").forward(request, response);
    }
}
