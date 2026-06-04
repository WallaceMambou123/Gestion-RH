<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="${not empty departement.id ? 'Modifier département' : 'Nouveau département'}"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
    <jsp:param name="page" value="departements"/>
</jsp:include>

<main class="main-content">
    <div class="page-header d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/departement?action=liste" class="btn btn-ghost btn-icon me-3">
            <i class="ti ti-arrow-left fs-3"></i>
        </a>
        <div>
            <h2 class="page-title">${not empty departement.id ? 'Modifier le département' : 'Nouveau département'}</h2>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/departement?action=liste">Départements</a></li>
                    <li class="breadcrumb-item active" aria-current="page">${not empty departement.id ? 'Édition' : 'Création'}</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row justify-content-center mt-4">
        <div class="col-lg-8">
            <div class="card p-lg-4">
                <div class="card-body">
                    <h5 class="card-title fw-bold mb-4 border-bottom pb-3">Informations Générales</h5>

                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <i class="ti ti-alert-triangle me-2 fs-4"></i>
                            <div>${requestScope.error}</div>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/departement" method="post" class="row g-4">
                        <input type="hidden" name="action" value="sauvegarder">
                        <input type="hidden" name="id"     value="${departement.id}">

                        <div class="col-12">
                            <label class="form-label fw-semibold">Nom du département <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="ti ti-building-community"></i></span>
                                <input type="text" name="nom" class="form-control"
                                       value="${departement.nom}" required
                                       placeholder="ex : Département Informatique">
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Responsable du département</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="ti ti-user-star"></i></span>
                                <input type="text" name="responsable" class="form-control"
                                       value="${departement.responsable}"
                                       placeholder="ex : Marc Dubois">
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-semibold">Budget Annuel (Masse Salariale)</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="ti ti-coin"></i></span>
                                <input type="number" name="budgetMasseSalariale" class="form-control"
                                       value="${departement.budgetMasseSalariale}" step="1000" min="0"
                                       placeholder="ex : 12000000">
                                <span class="input-group-text text-muted small">FCFA</span>
                            </div>
                        </div>

                        <div class="col-12 pt-4 border-top">
                            <div class="d-flex justify-content-between align-items-center">
                                <p class="text-muted small mb-0"><span class="text-danger">*</span> Champs obligatoires</p>
                                <div class="btn-group-actions">
                                    <a href="${pageContext.request.contextPath}/departement?action=liste" class="btn btn-light px-4 me-2 text-dark border">Annuler</a>
                                    <button type="submit" class="btn btn-primary px-5">
                                        <i class="ti ti-device-floppy me-2"></i>Enregistrer le département
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-lg-4 mt-4 mt-lg-0">
            <div class="card bg-primary bg-opacity-5 border-primary border-opacity-10 p-3">
                <div class="card-body">
                    <h6 class="fw-bold text-primary mb-3"><i class="ti ti-info-circle me-1"></i> Conseil RH</h6>
                    <p class="text-muted small mb-2">
                        Le budget de la masse salariale permet au système de générer des alertes si les nouveaux contrats dépassent les capacités du département.
                    </p>
                    <hr>
                    <p class="text-muted small mb-0">
                        Assurez-vous que le responsable désigné a bien les droits de "MANAGER" dans la gestion utilisateur pour accéder aux fiches et congés de son équipe.
                    </p>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
