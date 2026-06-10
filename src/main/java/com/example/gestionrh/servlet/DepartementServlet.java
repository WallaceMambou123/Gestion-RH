package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Departement;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.DepartementRepository;
import com.example.gestionrh.repository.EmployeRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/departement")
public class DepartementServlet extends HttpServlet {
    private final DepartementRepository repository = new DepartementRepository();
    private final EmployeRepository employeRepository = new EmployeRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;
        
        if (role == null || !role.equals("RH")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "liste":
                List<Departement> depts = repository.findAll();
                Map<Long, Long> counts = new HashMap<>();
                for (Departement d : depts) {
                    counts.put(d.getId(), employeRepository.countByDepartement(d.getId()));
                }
                request.setAttribute("departements", depts);
                request.setAttribute("totalEmployesParDept", counts);
                request.getRequestDispatcher("/WEB-INF/views/departement/list.jsp").forward(request, response);
                break;

            case "formulaire":
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isBlank()) {
                    request.setAttribute("departement", repository.findById(Long.parseLong(idParam)));
                } else {
                    request.setAttribute("departement", new Departement());
                }
                request.getRequestDispatcher("/WEB-INF/views/departement/form.jsp").forward(request, response);
                break;

            case "supprimer":
                try {
                    Long id = Long.parseLong(request.getParameter("id"));
                    repository.delete(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response.sendRedirect(request.getContextPath() + "/departement?action=liste&msg=supprime");
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/departement?action=liste");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;
        
        if (role == null || !role.equals("RH")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        if ("sauvegarder".equals(action)) {
            try {
                String idStr       = request.getParameter("id");
                String nom         = request.getParameter("nom");
                String responsable = request.getParameter("responsable");
                String budgetStr   = request.getParameter("budgetMasseSalariale");

                BigDecimal budget = null;
                if (budgetStr != null && !budgetStr.isBlank()) {
                    budget = new BigDecimal(budgetStr);
                }

                Departement d = Departement.builder()
                        .nom(nom)
                        .responsable(responsable)
                        .budgetMasseSalariale(budget)
                        .build();

                if (idStr != null && !idStr.isBlank()) {
                    d.setId(Long.parseLong(idStr));
                }

                repository.save(d);
                response.sendRedirect(request.getContextPath() + "/departement?action=liste&msg=sauvegarde");

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Erreur : " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/departement/form.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/departement?action=liste");
        }
    }
}
