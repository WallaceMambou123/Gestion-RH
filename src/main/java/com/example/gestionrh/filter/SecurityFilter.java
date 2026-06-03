package com.example.gestionrh.filter;

import com.example.gestionrh.model.Utilisateur;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {
    "/dashboard/*", "/employe/*", "/departement/*", 
    "/conge/*", "/paie/*", "/contrat/*"
})
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Chemins autorisés sans connexion (normalement déjà exclus par l'urlPattern mais sécurité double)
        String loginPath = httpRequest.getContextPath() + "/connexion";
        String registerPath = httpRequest.getContextPath() + "/inscription";

        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;

        if (user == null) {
            // Non connecté -> Redirection vers connexion
            httpResponse.sendRedirect(loginPath);
        } else {
            // Connecté -> Vérification sommaire des autorisations selon l'URI
            String uri = httpRequest.getRequestURI();
            
            // Exemple de restriction par rôle : seul RH peut accéder à /paie ou /contrat en écriture
            if ((uri.contains("/paie") || uri.contains("/contrat") || uri.contains("/departement")) 
                && user.getRole() == Utilisateur.Role.EMPLOYE) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/403.jsp");
                return;
            }
            
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
