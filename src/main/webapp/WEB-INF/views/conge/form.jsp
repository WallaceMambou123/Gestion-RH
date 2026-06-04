<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Nouvelle Demande de Congé"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
    <jsp:param name="page" value="conges"/>
</jsp:include>

<main class="main-content">
    <div class="page-header d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/conge?action=liste" class="btn btn-ghost btn-icon me-3">
            <i class="ti ti-arrow-left fs-3"></i>
        </a>
        <div>
            <h2 class="page-title">Demande de Congé</h2>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/conge?action=liste">Mes Congés</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Faire une demande</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-lg-8">
            <div class="card p-lg-4 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title fw-bold mb-4 border-bottom pb-3">Détails de l'absence</h5>

                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <i class="ti ti-alert-triangle me-2 fs-4"></i>
                            <div>${requestScope.error}</div>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/conge" method="post" class="row g-4">
                        <input type="hidden" name="action" value="demander">

                        <div class="col-md-12">
                            <label class="form-label fw-semibold">Type de congé <span class="text-danger">*</span></label>
                            <div class="row g-2">
                                <c:forEach var="type" items="${typesConge}">
                                    <div class="col-md-4">
                                        <input type="radio" name="typeConge" value="${type}" id="type_${type}" class="btn-check" required>
                                        <label class="btn btn-outline-secondary w-100 py-3 d-flex flex-column align-items-center" for="type_${type}">
                                            <i class="ti ti-${type == 'ANNUEL'?'calendar-stats':(type == 'MALADIE'?'medical-cross':'clock-pause')} fs-3 mb-2"></i>
                                            <span>${type}</span>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Date de début <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="ti ti-calendar-event"></i></span>
                                <input type="date" name="dateDebut" class="form-control" required id="dateDebut">
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Date de fin <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="ti ti-calendar-event"></i></span>
                                <input type="date" name="dateFin" class="form-control" required id="dateFin">
                            </div>
                        </div>

                        <div class="col-12">
                            <label class="form-label fw-semibold">Motif de l'absence</label>
                            <textarea name="motif" class="form-control" rows="4" 
                                      placeholder="Expliquez brièvement la raison de votre demande ou les détails organisationnels..."></textarea>
                        </div>

                        <div class="col-12 pt-4 border-top">
                            <div class="d-flex justify-content-between align-items-center">
                                <p class="text-muted small mb-0"><i class="ti ti-info-circle me-1"></i> Vos jours seront déduits après approbation.</p>
                                <div class="btn-group-actions">
                                    <a href="${pageContext.request.contextPath}/conge?action=liste" class="btn btn-light px-4 me-2 border">Annuler</a>
                                    <button type="submit" class="btn btn-primary px-5 fw-bold">
                                        <i class="ti ti-send me-2"></i>Envoyer la demande
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="card bg-dark text-white p-4 h-100 shadow">
                <div class="card-body">
                    <h5 class="fw-bold mb-4 d-flex align-items-center">
                        <i class="ti ti-chart-pie me-2 text-primary"></i> Votre Solde
                    </h5>
                    
                    <div class="text-center py-4">
                        <h1 class="display-1 fw-bold text-primary mb-0">${solde}</h1>
                        <p class="text-muted">jours d'absence restants</p>
                    </div>

                    <hr class="border-secondary opacity-50">

                    <ul class="list-unstyled mb-0">
                        <li class="d-flex justify-content-between mb-3">
                            <span class="text-muted">Type dominant</span>
                            <span class="fw-bold">Annuel</span>
                        </li>
                        <li class="d-flex justify-content-between mb-3">
                            <span class="text-muted">Période de calcul</span>
                            <span class="small">Jan 2026 - Déc 2026</span>
                        </li>
                        <li class="d-flex justify-content-between">
                            <span class="text-muted">Droit acquis / mois</span>
                            <span class="fw-bold text-success">+2.5</span>
                        </li>
                    </ul>

                    <div class="mt-5 p-3 rounded bg-white bg-opacity-10 border border-white border-opacity-10">
                        <small class="d-block mb-1 text-primary fw-bold"><i class="ti ti-discount-check me-1"></i> Important</small>
                        <small class="text-light opacity-75">
                            Toute demande doit être déposée au moins 48h à l'avance pour traitement par votre responsable hiérarchique.
                        </small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    // Petite validation frontend simple pour les dates
    const dateDebut = document.getElementById('dateDebut');
    const dateFin = document.getElementById('dateFin');

    dateDebut.addEventListener('change', () => {
        if(dateDebut.value) dateFin.min = dateDebut.value;
    });

    dateFin.addEventListener('change', () => {
        if(dateFin.value) dateDebut.max = dateFin.value;
    });
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
