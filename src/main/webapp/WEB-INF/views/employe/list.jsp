<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Liste des Employés" />
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />

<main class="main-content flex-grow-1">
    <div class="topbar d-flex justify-content-between align-items-center">
        <h5 class="m-0 fw-bold text-secondary">Répertoire des Employés</h5>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/export?type=employes" class="btn btn-sm btn-outline-secondary">
                <i class="fa-solid fa-file-csv me-1"></i> Export CSV
            </a>
            <a href="${pageContext.request.contextPath}/employe?action=new" class="btn btn-sm btn-primary">
                <i class="fa-solid fa-plus me-1"></i> Nouvel Employé
            </a>
        </div>
    </div>

    <div class="container-fluid">
        <div class="card p-0 overflow-hidden">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-4">Photo</th>
                            <th>Matricule</th>
                            <th>Nom & Prénom</th>
                            <th>Poste</th>
                            <th>Département</th>
                            <th>Contrat</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${employes}">
                            <tr>
                                <td class="ps-4">
                                    <c:choose>
                                        <c:when test="${not empty e.photoFilename}">
                                            <img src="${pageContext.request.contextPath}/uploads/photos/${e.photoFilename}" class="rounded-circle shadow-sm" width="40" height="40">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="https://ui-avatars.com/api/?name=${e.prenom}+${e.nom}&background=e2e8f0" class="rounded-circle shadow-sm" width="40" height="40">
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="fw-bold">${e.matricule}</td>
                                <td>
                                    <div class="fw-bold">${e.prenom} ${e.nom}</div>
                                    <small class="text-muted">${e.email}</small>
                                </td>
                                <td><span class="badge bg-light text-dark">${e.poste}</span></td>
                                <td>${e.departement.nom}</td>
                                <td>
                                    <c:set var="badgeClass" value="bg-primary" />
                                    <c:if test="${e.typeContrat == 'STAGE'}"><c:set var="badgeClass" value="bg-info text-dark" /></c:if>
                                    <c:if test="${e.typeContrat == 'CONSULTANT'}"><c:set var="badgeClass" value="bg-warning text-dark" /></c:if>
                                    <span class="badge ${badgeClass}">${e.typeContrat}</span>
                                </td>
                                <td class="text-end pe-4">
                                    <a href="${pageContext.request.contextPath}/employe?action=edit&id=${e.id}" class="btn btn-sm btn-light border" title="Modifier">
                                        <i class="fa-solid fa-pen-to-square"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/contrat?employeId=${e.id}" class="btn btn-sm btn-light border" title="Contrats">
                                        <i class="fa-solid fa-file-contract"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/paie?action=new&employeId=${e.id}" class="btn btn-sm btn-light border" title="Générer Paie">
                                        <i class="fa-solid fa-money-bill-wave"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            
            <!-- Pagination Simplifiée pour le TP -->
            <div class="card-footer bg-white py-3">
                <nav class="d-flex justify-content-between align-items-center">
                    <small class="text-muted">Affichage de 1 à ${employes.size()} sur ${employes.size()} employés</small>
                    <ul class="pagination pagination-sm m-0">
                        <li class="page-item disabled"><a class="page-link" href="#">Précédent</a></li>
                        <li class="page-item active"><a class="page-link" href="#">1</a></li>
                        <li class="page-item disabled"><a class="page-link" href="#">Suivant</a></li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
