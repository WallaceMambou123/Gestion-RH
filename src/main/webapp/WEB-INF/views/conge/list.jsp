<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Gestion des Congés" />
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />

<main class="main-content flex-grow-1">
    <div class="topbar d-flex justify-content-between align-items-center">
        <h5 class="m-0 fw-bold text-secondary">
            <c:choose>
                <c:when test="${param.action == 'my'}">Mes Demandes de Congés</c:when>
                <c:otherwise>Toutes les Demandes (RH)</c:otherwise>
            </c:choose>
        </h5>
        <c:if test="${sessionScope.role == 'EMPLOYE'}">
            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#requestModal">
                <i class="fa-solid fa-plus me-2"></i> Nouvelle Demande
            </button>
        </c:if>
    </div>

    <div class="container-fluid">
        <div class="card p-4">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <c:if test="${sessionScope.role != 'EMPLOYE'}">
                                <th>Employé</th>
                            </c:if>
                            <th>Début</th>
                            <th>Fin</th>
                            <th>Type</th>
                            <th>Statut</th>
                            <th class="text-end">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${conges}" var="conge">
                            <tr>
                                <c:if test="${sessionScope.role != 'EMPLOYE'}">
                                    <td>${conge.employe.prenom} ${conge.employe.nom}</td>
                                </c:if>
                                <td>${conge.dateDebut}</td>
                                <td>${conge.dateFin}</td>
                                <td><span class="badge bg-info text-dark">${conge.typeConge}</span></td>
                                <td>
                                    <span class="badge ${conge.statut == 'APPROUVE' ? 'bg-success' : (conge.statut == 'REFUSE' ? 'bg-danger' : 'bg-warning')}">
                                        ${conge.statut}
                                    </span>
                                </td>
                                <td class="text-end">
                                    <c:if test="${(sessionScope.role == 'RH' || sessionScope.role == 'MANAGER') && conge.statut == 'DEMANDE'}">
                                        <a href="${pageContext.request.contextPath}/conge?action=approve&id=${conge.id}" class="btn btn-sm btn-outline-success">
                                            <i class="fa-solid fa-check"></i>
                                        </a>
                                    </c:if>
                                    <button class="btn btn-sm btn-outline-secondary" title="Détails">
                                        <i class="fa-solid fa-eye"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty conges}">
                            <tr>
                                <td colspan="6" class="text-center py-4 text-muted">Aucune demande trouvée.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<!-- Modal pour nouvelle demande (Uniquement pour Employé) -->
<c:if test="${sessionScope.role == 'EMPLOYE'}">
<div class="modal fade" id="requestModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form action="${pageContext.request.contextPath}/conge" method="post" class="modal-content">
            <input type="hidden" name="action" value="request">
            <div class="modal-header">
                <h5 class="modal-title">Demander un congé</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label">Type de congé</label>
                    <select name="typeConge" class="form-select" required>
                        <option value="ANNUEL">Annuel</option>
                        <option value="MALADIE">Maladie</option>
                        <option value="FORMATION">Formation</option>
                        <option value="EXCEPTITIONNEL">Exceptionnel</option>
                    </select>
                </div>
                <div class="row">
                    <div class="col-6 mb-3">
                        <label class="form-label">Date Debut</label>
                        <input type="date" name="dateDebut" class="form-input" required>
                    </div>
                    <div class="col-6 mb-3">
                        <label class="form-label">Date Fin</label>
                        <input type="date" name="dateFin" class="form-input" required>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label">Motif</label>
                    <textarea name="motif" class="form-input" rows="3" placeholder="Optionnel..."></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-modal="hide">Annuler</button>
                <button type="submit" class="btn btn-primary">Soumettre la demande</button>
            </div>
        </form>
    </div>
</div>
</c:if>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
