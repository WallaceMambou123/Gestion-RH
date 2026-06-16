<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Tableau de Bord RH"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="main-content flex-grow-1">
    <div class="topbar d-flex justify-content-between align-items-center">
        <h5 class="m-0 fw-bold text-secondary">Aperçu de l'Entreprise</h5>
        <div class="user-info d-flex align-items-center gap-2">
            <span class="text-muted small">${sessionScope.utilisateur.prenom} ${sessionScope.utilisateur.nom}</span>
            <img src="https://ui-avatars.com/api/?name=${sessionScope.utilisateur.prenom}+${sessionScope.utilisateur.nom}&background=3b82f6&color=fff"
                 class="rounded-circle" width="35" height="35" alt="avatar">
        </div>
    </div>

    <div class="container-fluid">
        <%-- Alertes messages --%>
        <c:if test="${not empty requestScope.message}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fa-solid fa-circle-check me-2"></i>${requestScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <%-- KPI Cards --%>
        <div class="row g-4 mb-4">
            <div class="col-md-3">
                <div class="card stat-card p-4">
                    <small class="text-muted text-uppercase fw-bold">Effectif Total</small>
                    <h2 class="fw-bold mt-2 text-primary">${effectifTotal}</h2>
                    <small class="text-muted">Employés actifs</small>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card p-4" style="border-left-color:#f59e0b;">
                    <small class="text-muted text-uppercase fw-bold">Congés en attente</small>
                    <h2 class="fw-bold mt-2 text-warning">${congesEnAttente}</h2>
                    <small class="text-muted">À approuver</small>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card p-4" style="border-left-color:#10b981;">
                    <small class="text-muted text-uppercase fw-bold">Masse Salariale</small>
                    <h2 class="fw-bold mt-2 text-success" style="font-size:1.3rem;">
                        <fmt:formatNumber value="${masseSalarialeTotal}" pattern="#,##0"/> FCFA
                    </h2>
                    <small class="text-muted">Somme des salaires de base</small>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card p-4" style="border-left-color:#ef4444;">
                    <small class="text-muted text-uppercase fw-bold">Alertes CDD</small>
                    <h2 class="fw-bold mt-2 text-danger">${alertesCDD.size()}</h2>
                    <small class="text-muted">Expirant dans 30 jours</small>
                </div>
            </div>
        </div>

        <div class="row g-4 mb-4">
            <%-- Graphique masse salariale par département --%>
            <div class="col-lg-8">
                <div class="card p-4 h-100">
                    <h6 class="fw-bold mb-4">Masse Salariale par Département (Somme des salaires de base)</h6>
                    <div style="height: 300px;">
                        <canvas id="payrollChart"></canvas>
                    </div>
                </div>
            </div>
            <%-- Actions rapides --%>
            <div class="col-lg-4">
                <div class="card p-4 h-100">
                    <h6 class="fw-bold mb-4">Actions Rapides</h6>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/employe?action=new" class="btn btn-outline-primary text-start p-3">
                            <i class="fa-solid fa-user-plus me-2"></i>Ajouter un employé
                        </a>
                        <a href="${pageContext.request.contextPath}/conge" class="btn btn-outline-primary text-start p-3">
                            <i class="fa-solid fa-calendar-check me-2"></i>Gérer les congés
                            <c:if test="${congesEnAttente > 0}">
                                <span class="badge bg-warning text-dark ms-1">${congesEnAttente}</span>
                            </c:if>
                        </a>
                        <a href="${pageContext.request.contextPath}/paie?action=formulaire" class="btn btn-outline-primary text-start p-3">
                            <i class="fa-solid fa-money-bill-transfer me-2"></i>Générer la paie
                        </a>
                        <a href="${pageContext.request.contextPath}/stats" class="btn btn-outline-secondary text-start p-3">
                            <i class="fa-solid fa-chart-bar me-2"></i>Statistiques RH
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="row g-4 mb-4">
            <%-- Répartition par département --%>
            <div class="col-md-6">
                <div class="card p-4">
                    <h6 class="fw-bold mb-4">Répartition des Effectifs par Département</h6>
                    <div style="height: 250px;">
                        <canvas id="deptChart"></canvas>
                    </div>
                </div>
            </div>
            <%-- Répartition par contrat --%>
            <div class="col-md-6">
                <div class="card p-4">
                    <h6 class="fw-bold mb-4">Distribution des Types de Contrat</h6>
                    <div style="height: 250px;">
                        <canvas id="contractChart"></canvas>
                    </div>
                </div>
            </div>
        </div>

        <%-- Congés en attente --%>
        <c:if test="${not empty congesEnAttenteList}">
        <div class="card p-4 mb-4">
            <h6 class="fw-bold mb-3"><i class="fa-solid fa-clock me-2 text-warning"></i>Congés en Attente d'Approbation</h6>
            <div class="table-responsive">
                <table class="table table-sm table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>Employé</th>
                            <th>Type</th>
                            <th>Du</th>
                            <th>Au</th>
                            <th>Jours</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cg" items="${congesEnAttenteList}">
                            <tr>
                                <td class="fw-bold">${cg.employe.prenom} ${cg.employe.nom}</td>
                                <td><span class="badge bg-info text-dark">${cg.typeConge}</span></td>
                                <td>${cg.dateDebut}</td>
                                <td>${cg.dateFin}</td>
                                <td>${cg.nbJours}j</td>
                                <td class="text-end">
                                    <form action="${pageContext.request.contextPath}/conge" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="validate">
                                        <input type="hidden" name="id" value="${cg.id}">
                                        <input type="hidden" name="decision" value="APPROUVE">
                                        <button type="submit" class="btn btn-sm btn-success me-1" title="Approuver">
                                            <i class="fa-solid fa-check"></i>
                                        </button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/conge" method="post" class="d-inline">
                                        <input type="hidden" name="action" value="validate">
                                        <input type="hidden" name="id" value="${cg.id}">
                                        <input type="hidden" name="decision" value="REFUSE">
                                        <button type="submit" class="btn btn-sm btn-danger" title="Refuser">
                                            <i class="fa-solid fa-xmark"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        </c:if>
    </div>
</main>

<script>
const deptLabels = [<c:forEach var="entry" items="${masseSalariale}" varStatus="s">"${entry.key}"<c:if test="${!s.last}">,</c:if></c:forEach>];
const deptData   = [<c:forEach var="entry" items="${masseSalariale}" varStatus="s">${entry.value}<c:if test="${!s.last}">,</c:if></c:forEach>];

const labelsRepartition = [<c:forEach var="entry" items="${repartitionDepts}" varStatus="s">"${entry.key}"<c:if test="${!s.last}">,</c:if></c:forEach>];
const dataRepartition   = [<c:forEach var="entry" items="${repartitionDepts}" varStatus="s">${entry.value}<c:if test="${!s.last}">,</c:if></c:forEach>];

const labelsContrats = [<c:forEach var="entry" items="${repartitionContrats}" varStatus="s">"${entry.key}"<c:if test="${!s.last}">,</c:if></c:forEach>];
const dataContrats   = [<c:forEach var="entry" items="${repartitionContrats}" varStatus="s">${entry.value}<c:if test="${!s.last}">,</c:if></c:forEach>];

const chartColors = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'];

document.addEventListener("DOMContentLoaded", function() {
    // 1. Payroll Chart (Bar)
    const payrollCtx = document.getElementById('payrollChart');
    if (payrollCtx) {
        new Chart(payrollCtx.getContext('2d'), {
            type: 'bar',
            data: {
                labels: deptLabels,
                datasets: [{
                    label: 'Masse Salariale (FCFA)',
                    data: deptData,
                    backgroundColor: '#3b82f6',
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: { y: { beginAtZero: true } }
            }
        });
    }

    // 2. Department Effectif Chart (Doughnut)
    const deptCtx = document.getElementById('deptChart');
    if (deptCtx) {
        new Chart(deptCtx.getContext('2d'), {
            type: 'doughnut',
            data: {
                labels: labelsRepartition,
                datasets: [{
                    data: dataRepartition,
                    backgroundColor: chartColors,
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { position: 'right' } }
            }
        });
    }

    // 3. Contract Distribution Chart (Pie)
    const contractCtx = document.getElementById('contractChart');
    if (contractCtx) {
        new Chart(contractCtx.getContext('2d'), {
            type: 'pie',
            data: {
                labels: labelsContrats,
                datasets: [{
                    data: dataContrats,
                    backgroundColor: chartColors,
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { position: 'right' } }
            }
        });
    }
});
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
