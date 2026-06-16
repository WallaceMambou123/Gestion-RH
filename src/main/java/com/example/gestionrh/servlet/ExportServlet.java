package com.example.gestionrh.servlet;

import com.example.gestionrh.model.Conge;
import com.example.gestionrh.model.Employe;
import com.example.gestionrh.model.FichePaie;
import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.CongeRepository;
import com.example.gestionrh.repository.EmployeRepository;
import com.example.gestionrh.repository.FichePaieRepository;
import com.example.gestionrh.service.EmployeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/export")
public class ExportServlet extends HttpServlet {
    private final EmployeService      employeService      = new EmployeService();
    private final EmployeRepository employeRepository   = new EmployeRepository();
    private final CongeRepository     congeRepository     = new CongeRepository();
    private final FichePaieRepository fichePaieRepository = new FichePaieRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null) { response.sendRedirect(request.getContextPath() + "/connexion"); return; }
        
        String role = (String) session.getAttribute("role");
        if (role == null || role.equals("EMPLOYE")) {
            response.sendRedirect(request.getContextPath() + "/403"); return;
        }

        String type = request.getParameter("type");
        String dateStr = LocalDate.now().toString(); // yyyy-MM-dd

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        if ("employes".equals(type)) {
            response.setHeader("Content-Disposition", "attachment; filename=\"employes_" + dateStr + ".csv\"");
            
            List<Employe> list;
            if ("RH".equals(role)) {
                list = employeService.getAll();
            } else {
                // MANAGER: lister par département
                Optional<Employe> mgrOpt = employeRepository.findByUtilisateurId(user.getId());
                if (mgrOpt.isPresent()) {
                    list = employeService.getByDepartement(mgrOpt.get().getDepartement().getId());
                } else {
                    list = List.of();
                }
            }

            PrintWriter out = response.getWriter();
            // UTF-8 BOM for Excel
            out.write('\ufeff');
            // Explicitly tell Excel the separator is a semicolon
            out.println("sep=;");
            out.println("Matricule;Nom;Prenom;Poste;Departement;Type Contrat;Date Embauche;Salaire Base;Email");
            for (Employe e : list) {
                out.printf("%s;%s;%s;%s;%s;%s;%s;%s;%s\n",
                    e.getMatricule(), e.getNom(), e.getPrenom(), e.getPoste(),
                    (e.getDepartement() != null ? e.getDepartement().getNom() : ""),
                    e.getTypeContrat(), e.getDateEmbauche(), e.getSalaireBase(), e.getEmail());
            }

        } else if ("conges".equals(type)) {
            response.setHeader("Content-Disposition", "attachment; filename=\"conges_" + dateStr + ".csv\"");
            List<Conge> conges = congeRepository.findAll();
            
            PrintWriter out = response.getWriter();
            out.write('\ufeff');
            out.println("sep=;");
            out.println("ID;Employé;Type;Début;Fin;Jours;Statut");
            for (Conge c : conges) {
                out.printf("%d;%s %s;%s;%s;%s;%d;%s\n",
                    c.getId(), c.getEmploye().getNom(), c.getEmploye().getPrenom(),
                    c.getTypeConge(), c.getDateDebut(), c.getDateFin(),
                    c.getNbJours(), c.getStatut());
            }

        } else if ("paies".equals(type)) {
            if (!"RH".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/403"); return;
            }
            response.setHeader("Content-Disposition", "attachment; filename=\"fiches_paie_" + dateStr + ".csv\"");
            List<FichePaie> fiches = fichePaieRepository.findAll();
            
            PrintWriter out = response.getWriter();
            out.write('\ufeff');
            out.println("sep=;");
            out.println("Employé;Mois;Base;Heures Sup;Primes;Retenues;Brut;Net");
            for (FichePaie fp : fiches) {
                out.printf("%s %s;%s;%.0f;%.0f;%.0f;%.0f;%.0f;%.0f\n",
                    fp.getEmploye().getPrenom(), fp.getEmploye().getNom(),
                    fp.getMois(), fp.getSalaireBase(), fp.getHeuresSup(),
                    fp.getPrimes(), fp.getRetenues(), fp.getSalaireBrut(), fp.getSalaireNet());
            }
        }
    }
}
