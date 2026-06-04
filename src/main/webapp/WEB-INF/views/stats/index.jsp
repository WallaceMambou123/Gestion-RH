<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Statistiques RH"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
    <jsp:param name="page" value="stats"/>
</jsp:include>

<main class="main-content">
    <div class="page-header d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="page-title">Statistiques RH</h2>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Analytique</li>
                </ol>
            </nav>
        </div>
        <div class="d-flex gap-2">
            <span class="badge bg-secondary bg-opacity-10 text-secondary p-2 px-3 border border-secondary border-opacity-25 rounded-pill">
                Année fiscale ${anneeActuelle}
            </span>
        </div>
    </div>

    <%-- KPIs --%>
    <div class="row g-4 mb-4">
        <div class="col-sm-6 col-xl-3">
            <div class="card card-hover shadow-sm border-0 h-100">
                <div class="card-body p-4">
                    <div class="d-flex align-items-center mb-3">
                        <div class="icon-shape bg-primary bg-opacity-10 text-primary rounded-circle me-3">
                            <i class="ti ti-users fs-3"></i>
                        </div>
                        <span class="text-muted fw-semibold small text-uppercase">Effectif Total</span>
                    </div>
                    <h2 class="fw-bold mb-1">${effectifTotal}</h2>
                    <p class="text-muted small mb-0"><i class="ti ti-chart-arrows text-success me-1"></i>+2% vs mois dernier</p>
                </div>
            </div>
        </div>
        <div class="col-sm-6 col-xl-3">
            <div class="card card-hover shadow-sm border-0 h-100">
                <div class="card-body p-4">
                    <div class="d-flex align-items-center mb-3">
                        <div class="icon-shape bg-success bg-opacity-10 text-success rounded-circle me-3">
                            <i class="ti ti-coin fs-3"></i>
                        </div>
                        <span class="text-muted fw-semibold small text-uppercase">Masse Salariale</span>
                    </div>
                    <h2 class="fw-bold mb-1"><fmt:formatNumber value="${masseSalarialeTotal}" pattern="#,##0"/></h2>
                    <p class="text-muted small mb-0">FCFA mensuel brut</p>
                </div>
            </div>
        </div>
        <div class="col-sm-6 col-xl-3">
            <div class="card card-hover shadow-sm border-0 h-100">
                <div class="card-body p-4">
                    <div class="d-flex align-items-center mb-3">
                        <div class="icon-shape bg-danger bg-opacity-10 text-danger rounded-circle me-3">
                            <i class="ti ti-report-medical fs-3"></i>
                        </div>
                        <span class="text-muted fw-semibold small text-uppercase">Absentéisme</span>
                    </div>
                    <h2 class="fw-bold mb-1"><fmt:formatNumber value="${tauxAbsenteisme}" pattern="0.00"/>%</h2>
                    <p class="text-muted small mb-0">Moyenne maladie anuelle</p>
                </div>
            </div>
        </div>
        <div class="col-sm-6 col-xl-3">
            <div class="card card-hover shadow-sm border-0 h-100">
                <div class="card-body p-4">
                    <div class="d-flex align-items-center mb-3">
                        <div class="icon-shape bg-warning bg-opacity-10 text-warning rounded-circle me-3">
                            <i class="ti ti-alert-triangle fs-3"></i>
                        </div>
                        <span class="text-muted fw-semibold small text-uppercase">Alertes CDD</span>
                    </div>
                    <h2 class="fw-bold mb-1">${alertesCDD.size()}</h2>
                    <p class="text-muted small mb-0">Échéances à moins de 30j</p>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4 mb-4">
        <%-- Charts --%>
        <div class="col-lg-6">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white border-0 py-3">
                    <h5 class="fw-bold m-0"><i class="ti ti-chart-donut me-2 text-primary"></i>Contrats</h5>
                </div>
                <div class="card-body">
                    <canvas id="contratsChart" height="250"></canvas>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white border-0 py-3">
                    <h5 class="fw-bold m-0"><i class="ti ti-chart-bar me-2 text-primary"></i>Départements</h5>
                </div>
                <div class="card-body">
                    <canvas id="deptsChart" height="250"></canvas>
                </div>
            </div>
        </div>
    </div>

    <%-- Alert details table --%>
    <c:if test="${not empty alertesCDD}">
        <div class="card shadow-sm border-0 overflow-hidden">
            <div class="card-header bg-white py-3">
                <h5 class="fw-bold m-0 text-danger"><i class="ti ti-user-exclamation me-2"></i>Détails des CDD en fin de contrat</h5>
            </div>
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="bg-light">
                        <tr>
                            <th class="ps-4">Employé</th>
                            <th>Matricule</th>
                            <th>Date Fin</th>
                            <th>Statut</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${alertesCDD}">
                            <tr>
                                <td class="ps-4">
                                    <div class="d-flex align-items-center">
                                        <div class="avatar bg-primary bg-opacity-10 text-primary rounded-circle me-3">
                                            ${c.employe.prenom.charAt(0)}${c.employe.nom.charAt(0)}
                                        </div>
                                        <div>
                                            <div class="fw-bold">${c.employe.prenom} ${c.employe.nom}</div>
                                            <small class="text-muted">${c.employe.poste}</small>
                                        </div>
                                    </div>
                                </td>
                                <td><code class="text-primary fw-bold">${c.employe.matricule}</code></td>
                                <td>
                                    <span class="text-danger fw-bold"><i class="ti ti-calendar-off me-1"></i>${c.dateFin}</span>
                                </td>
                                <td><span class="badge bg-warning bg-opacity-10 text-warning px-3 pill">Expirant</span></td>
                                <td class="text-end pe-4">
                                    <a href="${pageContext.request.contextPath}/employe?action=formulaire&id=${c.employe.id}" class="btn btn-sm btn-ghost btn-icon">
                                        <i class="ti ti-edit"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
</main>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    const contratsLabels = [<c:forEach var="entry" items="${repartitionContrats}" varStatus="s">'${entry.key}'<c:if test="${!s.last}">,</c:if></c:forEach>];
    const contratsData   = [<c:forEach var="entry" items="${repartitionContrats}" varStatus="s">${entry.value}<c:if test="${!s.last}">,</c:if></c:forEach>];

    const deptsLabels = [<c:forEach var="entry" items="${repartitionDepts}" varStatus="s">'${entry.key}'<c:if test="${!s.last}">,</c:if></c:forEach>];
    const deptsData   = [<c:forEach var="entry" items="${repartitionDepts}" varStatus="s">${entry.value}<c:if test="${!s.last}">,</c:if></c:forEach>];

    const COLORS = ['#206bc4', '#4299e1', '#b9e3f9', '#f76707', '#ff922b', '#fab005'];

    document.addEventListener("DOMContentLoaded", function() {
        // Doughnut Chart
        new Chart(document.getElementById('contratsChart').getContext('2d'), {
            type: 'doughnut',
            data: {
                labels: contratsLabels,
                datasets: [{ 
                    data: contratsData, 
                    backgroundColor: COLORS,
                    hoverOffset: 4,
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'bottom', labels: { usePointStyle: true, padding: 20 } }
                }
            }
        });

        // Bar Chart
        new Chart(document.getElementById('deptsChart').getContext('2d'), {
            type: 'bar',
            data: {
                labels: deptsLabels,
                datasets: [{ 
                    label: 'Collaborateurs', 
                    data: deptsData, 
                    backgroundColor: '#206bc4',
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { beginAtZero: true, grid: { display: false }, ticks: { stepSize: 1 } },
                    x: { grid: { display: false } }
                }
            }
        });
    });
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
