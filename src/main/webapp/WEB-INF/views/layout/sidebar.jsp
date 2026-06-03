<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="sidebar">
    <div class="p-4">
        <h4 class="fw-bold"><i class="fa-solid fa-people-roof me-2"></i> GESTION RH</h4>
        <small class="text-muted">Connecté en tant que : <strong>${sessionScope.role}</strong></small>
    </div>
    
    <nav class="mt-2">
        <a href="${pageContext.request.contextPath}/dashboard" class="active"><i class="fa-solid fa-gauge me-2"></i> Dashboard</a>
        
        <!-- Accès RH uniquement -->
        <c:if test="${sessionScope.role == 'RH'}">
            <a href="${pageContext.request.contextPath}/departement"><i class="fa-solid fa-building me-2"></i> Départements</a>
            <a href="${pageContext.request.contextPath}/employe"><i class="fa-solid fa-users me-2"></i> Employés</a>
            <a href="${pageContext.request.contextPath}/paie"><i class="fa-solid fa-file-invoice-dollar me-2"></i> Paie</a>
        </c:if>
        
        <!-- Accès RH et MANAGER -->
        <c:if test="${sessionScope.role == 'RH' || sessionScope.role == 'MANAGER'}">
            <a href="${pageContext.request.contextPath}/conge"><i class="fa-solid fa-calendar-check me-2"></i> Congés</a>
            <a href="${pageContext.request.contextPath}/contrat/alerts"><i class="fa-solid fa-triangle-exclamation me-2"></i> Alertes Contrats</a>
        </c:if>
        
        <!-- Accès EMPLOYE -->
        <c:if test="${sessionScope.role == 'EMPLOYE'}">
            <a href="${pageContext.request.contextPath}/conge?action=my"><i class="fa-solid fa-calendar-plus me-2"></i> Mes Congés</a>
            <a href="${pageContext.request.contextPath}/paie?action=my"><i class="fa-solid fa-receipt me-2"></i> Mes Fiches de Paie</a>
        </c:if>

        <div class="mt-4 pt-4 border-top border-secondary">
            <a href="${pageContext.request.contextPath}/deconnexion" class="text-danger"><i class="fa-solid fa-right-from-bracket me-2"></i> Déconnexion</a>
        </div>
    </nav>
</div>
