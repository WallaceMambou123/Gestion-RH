package com.example.gestionrh.filter;

import com.example.gestionrh.model.Utilisateur;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {
    "/dashboard", "/dashboard/*",
    "/employe", "/employe/*",
    "/departement", "/departement/*",
    "/conge", "/conge/*",
    "/paie", "/paie/*",
    "/contrat", "/contrat/*",
    "/stats", "/stats/*",
    "/export", "/export/*"
})
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest  = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;

        if (user == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/connexion");
            return;
        }

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        Utilisateur.Role role = user.getRole();

        // RH uniquement : /departement, /stats, /export
        if ((uri.startsWith(contextPath + "/departement")
          || uri.startsWith(contextPath + "/stats")
          || uri.startsWith(contextPath + "/export"))
            && role != Utilisateur.Role.RH) {
            httpResponse.sendRedirect(contextPath + "/403");
            return;
        }

        // RH ou MANAGER uniquement : /paie (génération)
        if (uri.startsWith(contextPath + "/paie")
            && role == Utilisateur.Role.EMPLOYE) {
            // Les employés peuvent voir leurs fiches (?action=my) mais pas générer
            String actionParam = httpRequest.getParameter("action");
            if (!"my".equals(actionParam) && !"pdf".equals(actionParam)) {
                httpResponse.sendRedirect(contextPath + "/403");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig filterConfig) throws ServletException {}
    @Override public void destroy() {}
}
