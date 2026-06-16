package com.example.gestionrh.filter;

import com.example.gestionrh.model.Utilisateur;
import com.example.gestionrh.repository.UtilisateurRepository;
import com.example.gestionrh.service.CongeService;
import com.example.gestionrh.service.StatistiqueService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class SidebarFilter implements Filter {
    private final UtilisateurRepository utilisateurRepository = new UtilisateurRepository();
    private final CongeService congeService = new CongeService();
    private final StatistiqueService statistiqueService = new StatistiqueService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if (session != null && session.getAttribute("utilisateur") != null) {
            Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
            
            if (user.getRole() == Utilisateur.Role.RH) {
                request.setAttribute("nbComptesAttente", utilisateurRepository.countPending());
                request.setAttribute("nbCongesEnAttente", congeService.countEnAttente());
                request.setAttribute("nbCddExpirants", statistiqueService.getAlertesCDDExpirants().size());
            } else if (user.getRole() == Utilisateur.Role.MANAGER) {
                // Pour le manager, on ne compte que les congés de son département
                com.example.gestionrh.repository.EmployeRepository empRepo = new com.example.gestionrh.repository.EmployeRepository();
                empRepo.findByUtilisateurId(user.getId()).ifPresent(emp -> {
                    if (emp.getDepartement() != null) {
                        long count = congeService.listerParDepartement(emp.getDepartement().getId()).stream()
                                .filter(c -> c.getStatut() == com.example.gestionrh.model.Conge.StatutConge.DEMANDE)
                                .count();
                        request.setAttribute("nbCongesEnAttente", count);
                    }
                });
            }
        }

        chain.doFilter(request, response);
    }
}
