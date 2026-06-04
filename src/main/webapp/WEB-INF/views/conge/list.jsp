<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Gestion des Congés"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
    <jsp:param name="page" value="conges"/>
</jsp:include>

<main class="main-content">
    <div class="page-header d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="page-title">Gestion des Congés</h2>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Congés</li>
                </ol>
            </nav>
        </div>
        <c:if test="${sessionScope.role == 'EMPLOYE'}">
            <a href="${pageContext.request.contextPath}/conge?action=formulaire" class="btn btn-primary">
                <i class="ti ti-plus me-1"></i> Nouvelle demande
            </a>
        </c:if>
    </div>

    <%-- Filtres --%>
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-body p-3">
            <div class="d-flex align-items-center flex-wrap gap-2">
                <span class="text-muted small fw-bold text-uppercase me-2">Filtrer par statut :</span>
                <a href="${pageContext.request.contextPath}/conge?action=liste" 
                   class="btn btn-sm ${empty statutFiltre ? 'btn-primary' : 'btn-ghost'}">Tous</a>
                
                <a href="${pageContext.request.contextPath}/conge?action=liste&statut=DEMANDE" 
                   class="btn btn-sm ${statutFiltre == 'DEMANDE' ? 'btn-warning' : 'btn-ghost-warning'}">En attente</a>
                
                <a href="${pageContext.request.contextPath}/conge?action=liste&statut=APPROUVE" 
                   class="btn btn-sm ${statutFiltre == 'APPROUVE' ? 'btn-success' : 'btn-ghost-success'}">Approuvés</a>
                
                <a href="${pageContext.request.contextPath}/conge?action=liste&statut=REFUSE" 
                   class="btn btn-sm ${statutFiltre == 'REFUSE' ? 'btn-danger' : 'btn-ghost-danger'}">Refusés</a>
            </div>
        </div>
    </div>

    <div class="card shadow-sm border-0 overflow-hidden">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="bg-light">
                    <tr>
                        <c:if test="${sessionScope.role != 'EMPLOYE'}">
                            <th class="ps-4">Employé</th>
                        </c:if>
                        <th>Type</th>
                        <th class="text-center">Durée</th>
                        <th>Dates</th>
                        <th>Statut</th>
                        <th class="text-end pe-4">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${conges}" var="conge">
                        <tr>
                            <c:if test="${sessionScope.role != 'EMPLOYE'}">
                                <td class="ps-4">
                                    <div class="d-flex align-items-center">
                                        <div class="avatar-soft bg-primary rounded-circle me-3">
                                            <c:out value="${conge.employe.prenom.charAt(0)}"/><c:out value="${conge.employe.nom.charAt(0)}"/>
                                        </div>
                                        <div>
                                            <div class="fw-bold">${conge.employe.prenom} ${conge.employe.nom}</div>
                                            <small class="text-muted">${conge.employe.departement.nom}</small>
                                        </div>
                                    </div>
                                </td>
                            </c:if>
                            <td>
                                <span class="badge bg-secondary bg-opacity-10 text-secondary text-uppercase py-2 px-3">
                                    <i class="ti ti-bookmark me-1"></i>${conge.typeConge}
                                </span>
                            </td>
                            <td class="text-center">
                                <span class="fw-bold fs-5">${conge.nbJours}</span>
                                <small class="text-muted d-block" style="font-size: 0.7rem;">JOURS</small>
                            </td>
                            <td>
                                <div class="d-flex align-items-center text-dark">
                                    <span class="small">${conge.dateDebut}</span>
                                    <i class="ti ti-arrow-right mx-2 text-muted"></i>
                                    <span class="small">${conge.dateFin}</span>
                                </div>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${conge.statut == 'APPROUVE'}">
                                        <span class="badge bg-success bg-opacity-10 text-success border border-success border-opacity-25 px-3">
                                            <i class="ti ti-circle-check me-1"></i>Approuvé
                                        </span>
                                    </c:when>
                                    <c:when test="${conge.statut == 'REFUSE'}">
                                        <span class="badge bg-danger bg-opacity-10 text-danger border border-danger border-opacity-25 px-3">
                                            <i class="ti ti-circle-x me-1"></i>Refusé
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning bg-opacity-10 text-warning border border-warning border-opacity-25 px-3">
                                            <i class="ti ti-clock me-1"></i>En attente
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end pe-4">
                                <c:choose>
                                    <c:when test="${(sessionScope.role == 'RH' || sessionScope.role == 'MANAGER') && conge.statut == 'DEMANDE'}">
                                        <div class="btn-group">
                                            <form action="${pageContext.request.contextPath}/conge" method="post" class="d-inline">
                                                <input type="hidden" name="action" value="approuver">
                                                <input type="hidden" name="id"     value="${conge.id}">
                                                <button type="submit" class="btn btn-icon btn-ghost-success" title="Approuver">
                                                    <i class="ti ti-check fs-4"></i>
                                                </button>
                                            </form>
                                            <button type="button" class="btn btn-icon btn-ghost-danger" title="Refuser" 
                                                    onclick="openRefusalModal('${conge.id}')">
                                                <i class="ti ti-x fs-4"></i>
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-icon btn-ghost" title="Détails" onclick="alert('Motif : ${conge.motif}')">
                                            <i class="ti ti-info-circle fs-4"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty conges}">
                        <tr>
                            <td colspan="7" class="text-center py-5">
                                <div class="py-5">
                                    <i class="ti ti-calendar-off fs-1 text-muted opacity-25"></i>
                                    <h5 class="fw-bold mt-3 text-muted">Aucun historique trouvé</h5>
                                    <p class="text-muted small">Les demandes apparaîtront ici une fois soumises.</p>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>

<%-- Modal de refus --%>
<div class="modal fade" id="refusalModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <form action="${pageContext.request.contextPath}/conge" method="post" class="modal-content border-0 shadow">
            <input type="hidden" name="action" value="refuser">
            <input type="hidden" name="id"     id="refusalCongeId">
            <div class="modal-header bg-danger text-white">
                <h5 class="modal-title fw-bold"><i class="ti ti-circle-x me-2"></i>Motif du refus</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <p class="text-muted small mb-3">Veuillez indiquer la raison du refus pour informer le collaborateur.</p>
                <textarea name="motifRefus" class="form-control" rows="4" required placeholder="ex : Charge de travail trop importante sur cette période..."></textarea>
            </div>
            <div class="modal-footer border-0 p-3">
                <button type="button" class="btn btn-light border px-4" data-bs-dismiss="modal">Annuler</button>
                <button type="submit" class="btn btn-danger px-4">Confirmer le refus</button>
            </div>
        </form>
    </div>
</div>

<script>
function openRefusalModal(id) {
    document.getElementById('refusalCongeId').value = id;
    new bootstrap.Modal(document.getElementById('refusalModal')).show();
}
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
