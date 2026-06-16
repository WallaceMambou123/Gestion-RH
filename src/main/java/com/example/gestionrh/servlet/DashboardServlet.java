package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.EmployeRepository;
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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final StatistiqueService statistiqueService = new StatistiqueService();
    private final CongeService congeService = new CongeService();
    private final EmployeRepository employeRepository = new EmployeRepository();
    private final com.example.gestionrh.repository.UtilisateurRepository utilisateurRepository = new com.example.gestionrh.repository.UtilisateurRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        String jspView;

        switch (user.getRole()) {
            case RH -> {
                // Données pour le dashboard RH
                request.setAttribute("effectifTotal",      statistiqueService.countEmployes());
                request.setAttribute("congesEnAttente",    congeService.countEnAttente());
                request.setAttribute("masseSalariale",     statistiqueService.getMasseSalarialeParDepartement());
                request.setAttribute("masseSalarialeTotal",statistiqueService.getMasseSalarialeTotale());
                request.setAttribute("tauxAbsenteisme",    statistiqueService.getTauxAbsenteisme());
                request.setAttribute("repartitionContrats",statistiqueService.getRepartitionParTypeContrat());
                request.setAttribute("repartitionDepts",   statistiqueService.getRepartitionParDepartement());
                request.setAttribute("alertesCDD",         statistiqueService.getAlertesCDDExpirants());
                request.setAttribute("congesEnAttenteList",congeService.listerEnAttente());
                jspView = "/WEB-INF/views/dashboard/dashboard-rh.jsp";
            }
            case MANAGER -> {
                // Données pour le dashboard Manager
                request.setAttribute("tauxAbsenteisme",    statistiqueService.getTauxAbsenteisme());
                request.setAttribute("masseSalarialeTotal",statistiqueService.getMasseSalarialeTotale());
                request.setAttribute("effectifTotal",      statistiqueService.countEmployes());
                request.setAttribute("alertesCDD",         statistiqueService.getAlertesCDDExpirants());
                request.setAttribute("congesEnAttenteList",congeService.listerEnAttente());
                jspView = "/WEB-INF/views/dashboard/dashboard-manager.jsp";
            }
            default -> {
                // Données pour le dashboard Employé
                employeRepository.findByUtilisateurId(user.getId()).ifPresent(emp -> {
                    request.setAttribute("employeInfo", emp);
                    session.setAttribute("employeId", emp.getId());
                    request.setAttribute("mesConges", congeService.listerParEmploye(emp.getId()));
                });
                jspView = "/WEB-INF/views/dashboard/dashboard-employe.jsp";
            }
        }

        request.getRequestDispatcher(jspView).forward(request, response);
    }
}
