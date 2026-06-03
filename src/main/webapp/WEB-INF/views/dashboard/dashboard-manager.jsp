<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Tableau de Bord Manager" />
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />

<main class="main-content flex-grow-1">
    <div class="topbar d-flex justify-content-between align-items-center">
        <h5 class="m-0 fw-bold text-secondary">Aperçu de mon équipe</h5>
        <div class="user-info">
            <span class="me-2">${sessionScope.utilisateur.prenom} ${sessionScope.utilisateur.nom}</span>
            <img src="https://ui-avatars.com/api/?name=${sessionScope.utilisateur.prenom}+${sessionScope.utilisateur.nom}&background=3b82f6&color=fff" class="rounded-circle" width="35">
        </div>
    </div>

    <div class="container-fluid">
        <div class="row g-4 mb-4">
            <!-- Stats Cards -->
            <div class="col-md-4">
                <div class="card stat-card p-4">
                    <small class="text-muted text-uppercase fw-bold">Masse Salariale Équipe</small>
                    <h2 class="fw-bold mt-2 text-primary">${masseSalarialeTotal != null ? masseSalarialeTotal : "---"} FCFA</h2>
                    <small class="text-muted">Estimation</small>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card stat-card p-4" style="border-left-color: #ef4444;">
                    <small class="text-muted text-uppercase fw-bold">Taux d'Absentéisme</small>
                    <h2 class="fw-bold mt-2 text-danger">${tauxAbsenteisme != null ? tauxAbsenteisme : "0"}%</h2>
                    <small class="text-muted">Équipe</small>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card stat-card p-4" style="border-left-color: #10b981;">
                    <small class="text-muted text-uppercase fw-bold">Mon Équipe</small>
                    <h2 class="fw-bold mt-2 text-success">${effectifTotal != null ? effectifTotal : "---"}</h2>
                    <small class="text-muted">Employés sous ma gestion</small>
                </div>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-lg-8">
                <div class="card p-4">
                    <h5 class="fw-bold mb-4">Présence de l'équipe</h5>
                    <canvas id="presenceChart" height="250"></canvas>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card p-4">
                    <h5 class="fw-bold mb-4">Actions Prioritaires</h5>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/conge" class="btn btn-outline-primary text-start"><i class="fa-solid fa-calendar-check me-2"></i> Valider les congés</a>
                        <a href="${pageContext.request.contextPath}/contrat/alerts" class="btn btn-outline-warning text-start"><i class="fa-solid fa-triangle-exclamation me-2"></i> Alertes contrats</a>
                        <a href="${pageContext.request.contextPath}/employe" class="btn btn-outline-secondary text-start"><i class="fa-solid fa-users me-2"></i> Voir mon équipe</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const ctx = document.getElementById('presenceChart').getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Lun', 'Mar', 'Mer', 'Jeu', 'Ven'],
                datasets: [{
                    label: 'Présence %',
                    data: [95, 98, 92, 90, 85],
                    borderColor: '#3b82f6',
                    tension: 0.1
                }]
            }
        });
    });
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
