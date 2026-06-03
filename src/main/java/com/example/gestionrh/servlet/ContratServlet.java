package com.example.gestionrh.servlet;

import com.example.gestionrh.model.ContratEmploye;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.repository.ContratRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@WebServlet("/contrat")
public class ContratServlet extends HttpServlet {
    private final ContratRepository repository = new ContratRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("alerts".equals(action)) {
            request.setAttribute("contrats", repository.findCDDExpirantDans30Jours());
            request.getRequestDispatcher("/WEB-INF/views/contrat/list.jsp").forward(request, response);
        } else {
            Long employeId = Long.parseLong(request.getParameter("employeId"));
            request.setAttribute("contrats", repository.findByEmploye(employeId));
            request.getRequestDispatcher("/WEB-INF/views/contrat/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long employeId = Long.parseLong(request.getParameter("employeId"));
        String type = request.getParameter("typeContrat");
        LocalDate debut = LocalDate.parse(request.getParameter("dateDebut"));
        String finStr = request.getParameter("dateFin");
        BigDecimal salaire = new BigDecimal(request.getParameter("salaire"));
        String avantages = request.getParameter("avantages");

        ContratEmploye c = ContratEmploye.builder()
                .employe(Employe.builder().id(employeId).build())
                .typeContrat(ContratEmploye.TypeContrat.valueOf(type))
                .dateDebut(debut)
                .dateFin(finStr != null && !finStr.isEmpty() ? LocalDate.parse(finStr) : null)
                .salaire(salaire)
                .avantages(avantages)
                .build();
        
        repository.save(c);
        response.sendRedirect(request.getContextPath() + "/contrat?employeId=" + employeId);
    }
}
