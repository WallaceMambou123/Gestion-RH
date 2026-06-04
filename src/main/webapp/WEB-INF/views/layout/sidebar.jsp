<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="sidebar">
  <!-- Logo Area -->
  <div class="sidebar-brand">
    <div class="brand-icon">
      <i class="fa-solid fa-people-roof text-white"></i>
    </div>
    <div class="brand-text">
      <span class="brand-name">RH Manager</span>
      <span class="brand-sub">Entreprise SA</span>
    </div>
  </div>

  <!-- Navigation -->
  <nav class="sidebar-nav">

    <!-- Section Principal -->
    <div class="nav-section">Principal</div>
    <a href="${pageContext.request.contextPath}/dashboard" class="nav-item ${page == 'dashboard' ? 'active' : ''}">
      <i class="fa-solid fa-chart-line"></i> Dashboard
    </a>

    <!-- Section Gestion -->
    <div class="nav-section">Gestion RH</div>

    <c:if test="${sessionScope.role == 'RH' or sessionScope.role == 'MANAGER'}">
      <a href="${pageContext.request.contextPath}/employe?action=liste" class="nav-item ${page == 'employes' ? 'active' : ''}">
        <i class="fa-solid fa-users-viewfinder"></i> Employés
      </a>
      <a href="${pageContext.request.contextPath}/contrat?action=liste" class="nav-item ${page == 'contrats' ? 'active' : ''}">
        <i class="fa-solid fa-file-signature"></i> Contrats
        <c:if test="${nbCddExpirants > 0}">
          <span class="nav-badge bg-warning text-dark">${nbCddExpirants}</span>
        </c:if>
      </a>
    </c:if>

    <!-- Congés visible par tous -->
    <a href="${pageContext.request.contextPath}/conge?action=liste" class="nav-item ${page == 'conges' ? 'active' : ''}">
      <i class="fa-solid fa-calendar-check"></i> Congés
      <c:if test="${nbCongesEnAttente > 0}">
        <span class="nav-badge">${nbCongesEnAttente}</span>
      </c:if>
    </a>

    <c:if test="${sessionScope.role == 'RH' or sessionScope.role == 'MANAGER'}">
      <a href="${pageContext.request.contextPath}/paie?action=liste" class="nav-item ${page == 'paie' ? 'active' : ''}">
        <i class="fa-solid fa-file-invoice-dollar"></i> Fiches de paie
      </a>
    </c:if>

    <!-- Employé : ses fiches à lui -->
    <c:if test="${sessionScope.role == 'EMPLOYE'}">
      <a href="${pageContext.request.contextPath}/paie?action=liste" class="nav-item ${page == 'paie' ? 'active' : ''}">
        <i class="fa-solid fa-file-invoice-dollar"></i> Mes fiches de paie
      </a>
    </c:if>

    <!-- RH uniquement: Administration -->
    <c:if test="${sessionScope.role == 'RH'}">
      <div class="nav-section">Administration</div>
      <a href="${pageContext.request.contextPath}/departement?action=liste" class="nav-item ${page == 'departements' ? 'active' : ''}">
        <i class="fa-solid fa-sitemap"></i> Départements
      </a>
      <a href="${pageContext.request.contextPath}/stats" class="nav-item ${page == 'stats' ? 'active' : ''}">
        <i class="fa-solid fa-square-poll-vertical"></i> Statistiques
      </a>
      <a href="${pageContext.request.contextPath}/export?type=employes" class="nav-item">
        <i class="fa-solid fa-file-export"></i> Exports
      </a>
    </c:if>

  </nav>

  <!-- Sidebar Footer User Profile -->
  <div class="sidebar-footer">
    <div class="user-avatar text-white">
      ${sessionScope.utilisateur.username.toUpperCase().charAt(0)}
    </div>
    <div class="overflow-hidden">
      <div class="user-name text-white text-truncate" style="max-width: 140px;">${sessionScope.utilisateur.username}</div>
      <div class="user-role">${sessionScope.role}</div>
    </div>
    <a href="${pageContext.request.contextPath}/logout" 
       class="logout-btn" title="Déconnexion">
      <i class="fa-solid fa-right-from-bracket"></i>
    </a>
  </div>
</div>
