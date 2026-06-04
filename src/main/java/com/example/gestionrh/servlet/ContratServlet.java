package com.example.gestionrh.servlet;

import com.example.gestionrh.model.ContratEmploye;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.ContratRepository;
import com.example.gestionrh.repository.EmployeRepository;
import com.example.gestionrh.service.PaieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/contrat")
public class ContratServlet extends HttpServlet {
    private final ContratRepository repository    = new ContratRepository();
    private final EmployeRepository  employeRepo  = new EmployeRepository();
    private final PaieService        paieService  = new PaieService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null) { response.sendRedirect(request.getContextPath() + "/connexion"); return; }

        String action = request.getParameter("action");

        if ("new".equals(action)) {
            if (user.getRole() != Utilisateur.Role.RH) {
                response.sendRedirect(request.getContextPath() + "/403"); return;
            }
            request.setAttribute("employes", employeRepo.findAll());
            request.getRequestDispatcher("/WEB-INF/views/contrat/form.jsp").forward(request, response);

        } else if ("pdf".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            ContratEmploye c = repository.findById(id);
            if (c == null) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
            byte[] pdf = paieService.genererContratPDF(c);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                "attachment; filename=Contrat_" + c.getEmploye().getMatricule() + ".pdf");
            response.getOutputStream().write(pdf);

        } else if ("alerts".equals(action)) {
            request.setAttribute("contrats", repository.findCDDExpirantDans30Jours());
            request.getRequestDispatcher("/WEB-INF/views/contrat/list.jsp").forward(request, response);

        } else {
            // Liste selon rôle
            String employeIdStr = request.getParameter("employeId");
            List<ContratEmploye> contrats;
            if (employeIdStr != null && !employeIdStr.isBlank()) {
                contrats = repository.findByEmploye(Long.parseLong(employeIdStr));
            } else if (user.getRole() == Utilisateur.Role.RH) {
                contrats = repository.findAll();
            } else {
                // MANAGER / EMPLOYE : leurs propres contrats
                contrats = employeRepo.findByUtilisateurId(user.getId())
                    .map(e -> repository.findByEmploye(e.getId()))
                    .orElse(List.of());
            }
            request.setAttribute("contrats", contrats);
            request.getRequestDispatcher("/WEB-INF/views/contrat/list.jsp").forward(request, response);
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

        String action = request.getParameter("action");

        if ("supprimer".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            repository.delete(id);
            response.sendRedirect(request.getContextPath() + "/contrat?success=supprime");
            return;
        }

        // Sauvegarder contrat
        try {
            Long employeId = Long.parseLong(request.getParameter("employeId"));
            String type    = request.getParameter("typeContrat");
            LocalDate debut = LocalDate.parse(request.getParameter("dateDebut"));
            String finStr  = request.getParameter("dateFin");
            BigDecimal sal = new BigDecimal(request.getParameter("salaire"));
            String avantt  = request.getParameter("avantages");
            String idStr   = request.getParameter("id");

            ContratEmploye c = ContratEmploye.builder()
                    .employe(Employe.builder().id(employeId).build())
                    .typeContrat(ContratEmploye.TypeContrat.valueOf(type))
                    .dateDebut(debut)
                    .dateFin(finStr != null && !finStr.isBlank() ? LocalDate.parse(finStr) : null)
                    .salaire(sal)
                    .avantages(avantt)
                    .build();

            if (idStr != null && !idStr.isBlank()) c.setId(Long.parseLong(idStr));

            repository.save(c);
            response.sendRedirect(request.getContextPath() + "/contrat?success=sauvegarde&employeId=" + employeId);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur : " + e.getMessage());
            request.setAttribute("employes", employeRepo.findAll());
            request.getRequestDispatcher("/WEB-INF/views/contrat/form.jsp").forward(request, response);
        }
    }
}
