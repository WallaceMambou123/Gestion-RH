o<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Tableau de Bord RH" />
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />

<main class="main-content flex-grow-1">
    <div class="topbar d-flex justify-content-between align-items-center">
        <h5 class="m-0 fw-bold text-secondary">Aperçu de l'Entreprise</h5>
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
                    <small class="text-muted text-uppercase fw-bold">Masse Salariale Totale</small>
                    <h2 class="fw-bold mt-2 text-primary">${masseSalarialeTotal} FCFA</h2>
                    <small class="text-success"><i class="fa-solid fa-arrow-up"></i> +2.5% vs mois dernier</small>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card stat-card p-4" style="border-left-color: #ef4444;">
                    <small class="text-muted text-uppercase fw-bold">Taux d'Absentéisme</small>
                    <h2 class="fw-bold mt-2 text-danger">${tauxAbsenteisme}%</h2>
                    <small class="text-muted">Mois de Juin</small>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card stat-card p-4" style="border-left-color: #10b981;">
                    <small class="text-muted text-uppercase fw-bold">Effectif Total</small>
                    <h2 class="fw-bold mt-2 text-success">${effectifTotal}</h2>
                    <small class="text-muted">Employés actifs</small>
                </div>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-lg-8">
                <div class="card p-4">
                    <h5 class="fw-bold mb-4">Masse Salariale par Département</h5>
                    <canvas id="payrollChart" height="250"></canvas>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card p-4">
                    <h5 class="fw-bold mb-4">Actions Rapides</h5>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/employe?action=new" class="btn btn-outline-primary text-start"><i class="fa-solid fa-user-plus me-2"></i> Ajouter un employé</a>
                        <a href="${pageContext.request.contextPath}/conge" class="btn btn-outline-primary text-start"><i class="fa-solid fa-calendar-check me-2"></i> Gérer les congés</a>
                        <a href="${pageContext.request.contextPath}/paie" class="btn btn-outline-primary text-start"><i class="fa-solid fa-money-bill-transfer me-2"></i> Générer la paie</a>
                        <a href="${pageContext.request.contextPath}/export?type=employes" class="btn btn-outline-secondary text-start"><i class="fa-solid fa-file-csv me-2"></i> Export CSV Employés</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    // Exemple de script pour Chart.js
    document.addEventListener("DOMContentLoaded", function() {
        const ctx = document.getElementById('payrollChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Informatique', 'RH', 'Finance', 'Marketing'],
                datasets: [{
                    label: 'Masse Salariale (Milliers FCFA)',
                    data: [12500, 4500, 8900, 6200],
                    backgroundColor: '#3b82f6'
                }]
            }
        });
    });
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
