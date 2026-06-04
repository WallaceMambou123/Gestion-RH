package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Conge;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.service.CongeService;
import com.example.gestionrh.repository.EmployeRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/conge")
public class CongeServlet extends HttpServlet {
    private final CongeService      congeService      = new CongeService();
    private final EmployeRepository employeRepository = new EmployeRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null) { response.sendRedirect(request.getContextPath() + "/connexion"); return; }

        String action = request.getParameter("action");
        if (action == null) action = "liste";

        String role = (String) session.getAttribute("role");
        
        if ("liste".equals(action)) {
            List<Conge> conges;
            
            if ("RH".equals(role)) {
                conges = congeService.listerTous();
            } else if ("MANAGER".equals(role)) {
                Optional<Employe> mgrOpt = employeRepository.findByUtilisateurId(user.getId());
                if (mgrOpt.isPresent()) {
                    conges = congeService.listerParDepartement(mgrOpt.get().getDepartement().getId());
                } else {
                    conges = List.of();
                }
            } else {
                Optional<Employe> empOpt = employeRepository.findByUtilisateurId(user.getId());
                conges = empOpt.map(e -> congeService.listerParEmploye(e.getId())).orElse(List.of());
            }

            request.setAttribute("conges", conges);
            request.setAttribute("statutFiltre", request.getParameter("statut"));
            request.getRequestDispatcher("/WEB-INF/views/conge/list.jsp").forward(request, response);

        } else if ("formulaire".equals(action)) {
            Optional<Employe> empOpt = employeRepository.findByUtilisateurId(user.getId());
            if (empOpt.isPresent()) {
                request.setAttribute("solde", empOpt.get().getSoldeCongesJours());
            } else {
                request.setAttribute("solde", 0);
            }
            request.setAttribute("typesConge", Conge.TypeConge.values());
            request.getRequestDispatcher("/WEB-INF/views/conge/form.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null) { response.sendRedirect(request.getContextPath() + "/connexion"); return; }

        String action = request.getParameter("action");
        String role   = (String) session.getAttribute("role");

        if ("demander".equals(action)) {
            try {
                Optional<Employe> empOpt = employeRepository.findByUtilisateurId(user.getId());
                if (empOpt.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Employé non trouvé pour cet utilisateur.");
                    return;
                }
                
                LocalDate debut = LocalDate.parse(request.getParameter("dateDebut"));
                LocalDate fin   = LocalDate.parse(request.getParameter("dateFin"));
                String type     = request.getParameter("typeConge");
                String motif    = request.getParameter("motif");

                Conge c = Conge.builder()
                        .employe(empOpt.get())
                        .typeConge(Conge.TypeConge.valueOf(type))
                        .dateDebut(debut)
                        .dateFin(fin)
                        .motif(motif)
                        .build();

                congeService.demanderConge(c);
                response.sendRedirect(request.getContextPath() + "/conge?action=liste&msg=demande");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/conge?action=formulaire&error=invalid_data");
            }

        } else if ("approuver".equals(action)) {
            if (!"RH".equals(role) && !"MANAGER".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/403"); return;
            }
            Long id = Long.parseLong(request.getParameter("id"));
            congeService.approuverConge(id, user.getUsername());
            response.sendRedirect(request.getContextPath() + "/conge?action=liste&msg=approuve");

        } else if ("refuser".equals(action)) {
            if (!"RH".equals(role) && !"MANAGER".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/403"); return;
            }
            Long id = Long.parseLong(request.getParameter("id"));
            String motifRefus = request.getParameter("motifRefus");
            congeService.refuserConge(id, motifRefus, user.getUsername());
            response.sendRedirect(request.getContextPath() + "/conge?action=liste&msg=refuse");
        }
    }
}
