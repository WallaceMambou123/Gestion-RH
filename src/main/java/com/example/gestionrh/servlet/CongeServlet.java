package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Conge;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.service.CongeService;
import com.example.gestionrh.repository.CongeRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import com.example.gestionrh.repository.EmployeRepository;

@WebServlet("/conge")
public class CongeServlet extends HttpServlet {
    private final CongeService congeService = new CongeService();
    private final CongeRepository congeRepository = new CongeRepository();
    private final EmployeRepository employeRepository = new EmployeRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        if ("approve".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("conge", congeRepository.findById(id));
            request.getRequestDispatcher("/WEB-INF/views/conge/approuver.jsp").forward(request, response);
        } else if ("my".equals(action)) {
            Optional<Employe> empOpt = employeRepository.findByUtilisateurId(user.getId());
            if (empOpt.isPresent()) {
                request.setAttribute("conges", congeRepository.findByEmploye(empOpt.get().getId()));
            }
            request.getRequestDispatcher("/WEB-INF/views/conge/list.jsp").forward(request, response);
        } else {
            request.setAttribute("conges", congeRepository.findByStatut(Conge.StatutConge.DEMANDE));
            request.getRequestDispatcher("/WEB-INF/views/conge/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");

        if ("request".equals(action)) {
            // Un employé demande un congé
            LocalDate debut = LocalDate.parse(request.getParameter("dateDebut"));
            LocalDate fin = LocalDate.parse(request.getParameter("dateFin"));
            String type = request.getParameter("typeConge");
            String motif = request.getParameter("motif");
            
            // On suppose que l'utilisateur est lié à un employe.
            // Si le champ employeId est vide, on le récupère via l'utilisateur connecté.
            Long employeId = null;
            String employeIdParam = request.getParameter("employeId");
            if (employeIdParam != null && !employeIdParam.isBlank()) {
                employeId = Long.parseLong(employeIdParam);
            } else if (user != null) {
                Optional<Employe> employeOpt = employeRepository.findByUtilisateurId(user.getId());
                if (employeOpt.isPresent()) {
                    employeId = employeOpt.get().getId();
                }
            }
            if (employeId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Employé introuvable pour la demande de congé.");
                return;
            }
            
            Conge c = Conge.builder()
                    .employe(Employe.builder().id(employeId).build())
                    .dateDebut(debut)
                    .dateFin(fin)
                    .typeConge(Conge.TypeConge.valueOf(type))
                    .motif(motif)
                    .build();
            congeService.demanderConge(c);
            
        } else if ("validate".equals(action)) {
            // RH ou Manager valide
            Long id = Long.parseLong(request.getParameter("id"));
            String decision = request.getParameter("decision");
            if ("APPROUVE".equals(decision)) {
                congeService.approuverConge(id, user.getUsername());
            } else {
                congeService.refuserConge(id, request.getParameter("motifRefus"), user.getUsername());
            }
        }
        response.sendRedirect(request.getContextPath() + "/conge");
    }
}
