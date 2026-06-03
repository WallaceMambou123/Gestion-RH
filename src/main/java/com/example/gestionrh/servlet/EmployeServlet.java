package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Departement;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.service.EmployeService;
import com.example.gestionrh.repository.DepartementRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/employe")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class EmployeServlet extends HttpServlet {
    private final EmployeService employeService = new EmployeService();
    private final DepartementRepository departementRepository = new DepartementRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("new".equals(action)) {
            request.setAttribute("departements", departementRepository.findAll());
            request.getRequestDispatcher("/WEB-INF/views/employe/form.jsp").forward(request, response);
        } else if ("edit".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("employe", employeService.getById(id));
            request.setAttribute("departements", departementRepository.findAll());
            request.getRequestDispatcher("/WEB-INF/views/employe/form.jsp").forward(request, response);
        } else {
            List<Employe> employes = employeService.getAll();
            request.setAttribute("employes", employes);
            request.getRequestDispatcher("/WEB-INF/views/employe/list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            String matricule = request.getParameter("matricule");
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String poste = request.getParameter("poste");
            Long deptId = Long.parseLong(request.getParameter("departementId"));
            LocalDate dateEmbauche = LocalDate.parse(request.getParameter("dateEmbauche"));
            BigDecimal salaireBase = new BigDecimal(request.getParameter("salaireBase"));
            String typeContrat = request.getParameter("typeContrat");
            String email = request.getParameter("email");
            String telephone = request.getParameter("telephone");

            Departement dept = departementRepository.findById(deptId);
            
            Employe.EmployeBuilder builder = Employe.builder()
                    .matricule(matricule)
                    .nom(nom)
                    .prenom(prenom)
                    .poste(poste)
                    .departement(dept)
                    .dateEmbauche(dateEmbauche)
                    .salaireBase(salaireBase)
                    .typeContrat(Employe.TypeContrat.valueOf(typeContrat))
                    .email(email)
                    .telephone(telephone);
            
            if (idStr != null && !idStr.isEmpty()) {
                builder.id(Long.parseLong(idStr));
            }

            // Gestion de l'upload photo
            Part filePart = request.getPart("photo");
            if (filePart != null && filePart.getSize() > 0) {
                String uploadPath = getServletContext().getRealPath("/") + "uploads/photos";
                String fileName = employeService.uploadPhoto(filePart.getInputStream(), filePart.getSubmittedFileName(), uploadPath);
                builder.photoFilename(fileName);
            }

            employeService.save(builder.build());
            response.sendRedirect(request.getContextPath() + "/employe");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}
