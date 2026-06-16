package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.FichePaie;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.EmployeRepository;
import com.example.gestionrh.repository.FichePaieRepository;
import com.example.gestionrh.service.EmployeService;
import com.example.gestionrh.service.PaieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/paie")
public class FichePaieServlet extends HttpServlet {
    private final PaieService          paieService         = new PaieService();
    private final EmployeService       employeService      = new EmployeService();
    private final FichePaieRepository  fichePaieRepository = new FichePaieRepository();
    private final EmployeRepository    employeRepository   = new EmployeRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null) { response.sendRedirect(request.getContextPath() + "/connexion"); return; }

        String action = request.getParameter("action");
        String role   = user.getRole().name();

        if ("formulaire".equals(action)) {
            if (!role.equals("RH")) { response.sendRedirect(request.getContextPath() + "/403"); return; }
            request.setAttribute("employes", employeService.getAll());
            request.getRequestDispatcher("/WEB-INF/views/paie/form.jsp").forward(request, response);

        } else if ("pdf".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                Long id = Long.parseLong(idStr);
                FichePaie fp = fichePaieRepository.findById(id);
                if (fp != null) {
                    byte[] pdf = paieService.genererFichePaiePDF(fp);
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "attachment; filename=Bulletin_" + fp.getMois() + "_" + fp.getEmploye().getNom() + ".pdf");
                    response.getOutputStream().write(pdf);
                    return;
                }
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Fiche de paie introuvable.");
            return;

        } else if ("my".equals(action)) {
            // Employé consulte ses propres fiches
            employeRepository.findByUtilisateurId(user.getId()).ifPresent(emp -> {
                request.setAttribute("fichesPayie", fichePaieRepository.findByEmploye(emp.getId()));
            });
            request.getRequestDispatcher("/WEB-INF/views/paie/list.jsp").forward(request, response);

        } else {
            // Liste globale
            String moisFilter = request.getParameter("mois");
            List<FichePaie> fiches;
            if (moisFilter != null && !moisFilter.isBlank()) {
                fiches = fichePaieRepository.findByMois(moisFilter);
            } else if (role.equals("EMPLOYE")) {
                fiches = employeRepository.findByUtilisateurId(user.getId())
                    .map(e -> fichePaieRepository.findByEmploye(e.getId()))
                    .orElse(List.of());
            } else {
                fiches = fichePaieRepository.findAll();
            }
            request.setAttribute("fichesPayie", fiches);
            request.getRequestDispatcher("/WEB-INF/views/paie/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null || user.getRole() != Utilisateur.Role.RH) {
            response.sendRedirect(request.getContextPath() + "/403"); return;
        }

        try {
            Long employeId     = Long.parseLong(request.getParameter("employeId"));
            String mois        = request.getParameter("mois");
            BigDecimal hs      = new BigDecimal(request.getParameter("heuresSup"));
            BigDecimal primes  = new BigDecimal(request.getParameter("primes"));
            BigDecimal retenues = new BigDecimal(request.getParameter("retenues"));

            if (fichePaieRepository.existsByEmployeAndMois(employeId, mois)) {
                request.setAttribute("error", "Une fiche de paie existe déjà pour cet employé ce mois-ci.");
                request.setAttribute("employes", employeService.getAll());
                request.getRequestDispatcher("/WEB-INF/views/paie/form.jsp").forward(request, response);
                return;
            }

            Employe e = employeService.getById(employeId);
            if (e == null) {
                request.setAttribute("error", "Employé introuvable.");
                request.setAttribute("employes", employeService.getAll());
                request.getRequestDispatcher("/WEB-INF/views/paie/form.jsp").forward(request, response);
                return;
            }

            paieService.calculerFichePaie(e, mois, hs, primes, retenues);
            response.sendRedirect(request.getContextPath() + "/paie?mois=" + mois + "&success=genere");

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Erreur de calcul : " + ex.getMessage());
            request.setAttribute("employes", employeService.getAll());
            request.getRequestDispatcher("/WEB-INF/views/paie/form.jsp").forward(request, response);
        }
    }
}
