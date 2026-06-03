<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Mon Espace" />
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />

<main class="main-content flex-grow-1">
    <div class="topbar d-flex justify-content-between align-items-center">
        <h5 class="m-0 fw-bold text-secondary">Bienvenue dans votre espace RH</h5>
        <div class="user-info">
            <span class="me-2">${sessionScope.utilisateur.prenom} ${sessionScope.utilisateur.nom}</span>
            <img src="https://ui-avatars.com/api/?name=${sessionScope.utilisateur.prenom}+${sessionScope.utilisateur.nom}&background=3b82f6&color=fff" class="rounded-circle" width="35">
        </div>
    </div>

    <div class="container-fluid">
        <div class="row g-4 mb-4">
            <!-- Stats Cards -->
            <div class="col-md-6">
                <div class="card stat-card p-4">
                    <small class="text-muted text-uppercase fw-bold">Solde Congés</small>
                    <h2 class="fw-bold mt-2 text-primary">${disponibleConge != null ? disponibleConge : 25} Jours</h2>
                    <small class="text-muted">A prendre sur l'année en cours</small>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card stat-card p-4" style="border-left-color: #10b981;">
                    <small class="text-muted text-uppercase fw-bold">Dernière Fiche de Paie</small>
                    <h2 class="fw-bold mt-2 text-success">Disponible</h2>
                    <small class="text-muted">Mois de Mai</small>
                </div>
            </div>
        </div>

        <div class="row g-4">
            <div class="col-lg-8">
                <div class="card p-4">
                    <h5 class="fw-bold mb-4">Mes Informations de Profil</h5>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="text-muted small d-block">Nom d'utilisateur</label>
                            <strong>${sessionScope.utilisateur.username}</strong>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="text-muted small d-block">Email</label>
                            <strong>${sessionScope.utilisateur.email}</strong>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="text-muted small d-block">Poste</label>
                            <strong>${employeInfo != null ? employeInfo.poste : "Non défini"}</strong>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="text-muted small d-block">Département</label>
                            <strong>${employeInfo != null ? employeInfo.departement.nom : "Non défini"}</strong>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card p-4">
                    <h5 class="fw-bold mb-4">Accès Rapides</h5>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/conge?action=my" class="btn btn-outline-primary text-start"><i class="fa-solid fa-calendar-plus me-2"></i> Demander un congé</a>
                        <a href="${pageContext.request.contextPath}/paie?action=my" class="btn btn-outline-primary text-start"><i class="fa-solid fa-receipt me-2"></i> Mes fiches de paie</a>
                        <a href="#" class="btn btn-outline-secondary text-start"><i class="fa-solid fa-user-pen me-2"></i> Modifier mon profil</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
