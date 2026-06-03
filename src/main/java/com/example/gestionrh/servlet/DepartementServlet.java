package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Departement;
import com.example.gestionrh.repository.DepartementRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/departement")
public class DepartementServlet extends HttpServlet {
    private final DepartementRepository repository = new DepartementRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("new".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/departement/form.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("departement", repository.findById(id));
            request.getRequestDispatcher("/WEB-INF/views/departement/form.jsp").forward(request, response);
        } else {
            request.setAttribute("departements", repository.findAll());
            request.getRequestDispatcher("/WEB-INF/views/departement/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String nom = request.getParameter("nom");
        String responsable = request.getParameter("responsable");
        BigDecimal budget = new BigDecimal(request.getParameter("budget"));

        Departement d = Departement.builder()
                .nom(nom)
                .responsable(responsable)
                .budgetMasseSalariale(budget)
                .build();
        
        if (idStr != null && !idStr.isEmpty()) d.setId(Long.parseLong(idStr));
        
        repository.save(d);
        response.sendRedirect(request.getContextPath() + "/departement");
    }
}
