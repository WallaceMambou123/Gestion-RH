package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.FichePaie;
import com.example.gestionrh.service.EmployeService;
import com.example.gestionrh.service.PaieService;
import com.example.gestionrh.repository.FichePaieRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/paie")
public class FichePaieServlet extends HttpServlet {
    private final PaieService paieService = new PaieService();
    private final EmployeService employeService = new EmployeService();
    private final FichePaieRepository fichePaieRepository = new FichePaieRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("pdf".equals(action)) {
            Long employeId = Long.parseLong(request.getParameter("employeId"));
            String mois = request.getParameter("mois");
            Optional<FichePaie> fpOpt = fichePaieRepository.findByEmployeAndMois(employeId, mois);
            
            if (fpOpt.isPresent()) {
                byte[] pdf = paieService.genererFichePaiePDF(fpOpt.get());
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=FichePaie_" + mois + ".pdf");
                response.getOutputStream().write(pdf);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Fiche de paie non trouvée.");
            }
        } else {
            // Vue par défaut : liste des fiches pour le mois en cours
            request.getRequestDispatcher("/WEB-INF/views/paie/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Enregistrement d'une nouvelle fiche de paie
        Long employeId = Long.parseLong(request.getParameter("employeId"));
        String mois = request.getParameter("mois");
        BigDecimal hs = new BigDecimal(request.getParameter("heuresSup"));
        BigDecimal primes = new BigDecimal(request.getParameter("primes"));
        BigDecimal retenues = new BigDecimal(request.getParameter("retenues"));

        Employe e = employeService.getById(employeId);
        paieService.calculerFichePaie(e, mois, hs, primes, retenues);
        
        response.sendRedirect(request.getContextPath() + "/paie?mois=" + mois);
    }
}
