package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Departement;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.service.EmployeService;
import com.example.gestionrh.repository.DepartementRepository;
import com.example.gestionrh.repository.EmployeRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/employe")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 25)
public class EmployeServlet extends HttpServlet {
    private final EmployeService employeService = new EmployeService();
    private final DepartementRepository departementRepository = new DepartementRepository();
    private final EmployeRepository employeRepository = new EmployeRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null) { response.sendRedirect(request.getContextPath() + "/connexion"); return; }

        String action = request.getParameter("action");
        String role   = user.getRole().name();

        if ("new".equals(action) || "formulaire".equals(action)) {
            if (!"RH".equals(role)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            List<Departement> depts = departementRepository.findAll();
            request.setAttribute("departements", depts);
            request.getRequestDispatcher("/WEB-INF/views/employe/form.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            if (!role.equals("RH")) { response.sendRedirect(request.getContextPath() + "/403"); return; }
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("employe", employeService.getById(id));
            request.setAttribute("departements", departementRepository.findAll());
            request.getRequestDispatcher("/WEB-INF/views/employe/form.jsp").forward(request, response);

        } else if ("export".equals(action)) {
            if (role.equals("EMPLOYE")) { response.sendRedirect(request.getContextPath() + "/403"); return; }
            List<Employe> employes;
            if (role.equals("MANAGER")) {
                Long deptId = (Long) session.getAttribute("departementId");
                employes = (deptId != null) ? employeService.getByDepartement(deptId) : employeService.getAll();
            } else {
                employes = employeService.getAll();
            }
            String csv = employeService.exportToCSV(employes);
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=employes.csv");
            response.getWriter().write(csv);

        } else {
            // Liste principale
            List<Employe> employes;
            String search = request.getParameter("search");
            if (search != null && !search.isBlank()) {
                employes = employeService.rechercherParNom(search);
                request.setAttribute("search", search);
            } else if (role.equals("MANAGER")) {
                Long deptId = (Long) session.getAttribute("departementId");
                employes = (deptId != null) ? employeService.getByDepartement(deptId) : employeService.getAll();
            } else if (role.equals("EMPLOYE")) {
                response.sendRedirect(request.getContextPath() + "/403"); return;
            } else {
                employes = employeService.getAll();
            }
            request.setAttribute("employes", employes);
            request.setAttribute("totalEmployes", employeService.count());
            request.getRequestDispatcher("/WEB-INF/views/employe/list.jsp").forward(request, response);
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
            employeService.delete(id);
            response.sendRedirect(request.getContextPath() + "/employe?success=supprime");
            return;
        }

        // Sauvegarde (création ou modification)
        try {
            String idStr     = request.getParameter("id");
            String nom       = request.getParameter("nom");
            String prenom    = request.getParameter("prenom");
            String poste     = request.getParameter("poste");
            Long   deptId    = Long.parseLong(request.getParameter("departementId"));
            LocalDate date   = LocalDate.parse(request.getParameter("dateEmbauche"));
            BigDecimal sal   = new BigDecimal(request.getParameter("salaireBase"));
            String type      = request.getParameter("typeContrat");
            String email     = request.getParameter("email");
            String tel       = request.getParameter("telephone");
            String matricule = request.getParameter("matricule");

            Departement dept = departementRepository.findById(deptId);

            Employe.EmployeBuilder builder = Employe.builder()
                    .nom(nom).prenom(prenom).poste(poste)
                    .departement(dept).dateEmbauche(date)
                    .salaireBase(sal)
                    .typeContrat(Employe.TypeContrat.valueOf(type))
                    .email(email).telephone(tel);

            if (idStr != null && !idStr.isBlank()) {
                builder.id(Long.parseLong(idStr));
                if (matricule != null && !matricule.isBlank()) builder.matricule(matricule);
            }

            // Photo upload
            Part filePart = request.getPart("photo");
            if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null
                    && !filePart.getSubmittedFileName().isBlank()) {
                String uploadPath = getServletContext().getRealPath("/") + "uploads/photos";
                String fileName = employeService.uploadPhoto(
                        filePart.getInputStream(), filePart.getSubmittedFileName(), uploadPath);
                builder.photoFilename(fileName);
            } else if (idStr != null && !idStr.isBlank()) {
                // Conserver l'ancienne photo si pas de nouvelle
                Employe existing = employeService.getById(Long.parseLong(idStr));
                if (existing != null && existing.getPhotoFilename() != null) {
                    builder.photoFilename(existing.getPhotoFilename());
                }
            }

            employeService.save(builder.build());

            // Redirection Post-Redirect-Get propre
            response.sendRedirect(request.getContextPath() + "/employe?success=sauvegarde");

        } catch (Exception e) {
            e.printStackTrace();
            // Réinjection des données indispensables pour ré-afficher le formulaire en cas de plantage
            request.setAttribute("error", "Erreur lors de la sauvegarde : " + e.getMessage());
            request.setAttribute("departements", departementRepository.findAll());

            // Optionnel : reconstruire un objet temporaire à renvoyer à la vue pour ne pas vider les inputs
            request.getRequestDispatcher("/WEB-INF/views/employe/form.jsp").forward(request, response);
        }
    }
}