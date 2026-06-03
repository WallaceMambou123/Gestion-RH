package com.example.gestionrh.servlet;

import com.example.gestionrh.service.EmployeService;
import com.example.gestionrh.model.Employe;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/export")
public class ExportServlet extends HttpServlet {
    private final EmployeService employeService = new EmployeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        
        if ("employes".equals(type)) {
            List<Employe> list = employeService.getAll();
            String csv = employeService.exportToCSV(list);
            
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=employes.csv");
            response.getWriter().write(csv);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type d'export non supporté.");
        }
    }
}
