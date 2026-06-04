<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Gestion des Contrats"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="main-content flex-grow-1">
    <div class="topbar d-flex justify-content-between align-items-center">
        <h5 class="m-0 fw-bold text-secondary"><i class="fa-solid fa-file-contract me-2"></i>Contrats</h5>
        <c:if test="${sessionScope.role == 'RH'}">
            <a href="${pageContext.request.contextPath}/contrat?action=new" class="btn btn-sm btn-primary">
                <i class="fa-solid fa-plus me-1"></i>Nouveau Contrat
            </a>
        </c:if>
    </div>

    <div class="container-fluid">
        <c:if test="${not empty requestScope.message}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fa-solid fa-circle-check me-2"></i>${requestScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="card p-0 overflow-hidden">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-4">Employé</th>
                            <th>Type</th>
                            <th>Début</th>
                            <th>Fin</th>
                            <th>Salaire</th>
                            <th>Expiration</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${contrats}">
                            <c:set var="daysLeft" value="9999"/>
                            <c:if test="${not empty c.dateFin}">
                                <%-- Calcul approximatif affiché, la logique exacte est dans le service --%>
                            </c:if>
                            <tr>
                                <td class="ps-4 fw-bold">
                                    ${c.employe.prenom} ${c.employe.nom}
                                    <br><small class="text-muted">${c.employe.matricule}</small>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${c.typeContrat == 'CDI'}">
                                            <span class="badge bg-success">CDI</span>
                                        </c:when>
                                        <c:when test="${c.typeContrat == 'CDD'}">
                                            <span class="badge bg-warning text-dark">CDD</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-info text-dark">${c.typeContrat}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${c.dateDebut}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty c.dateFin}">
                                            ${c.dateFin}
                                        </c:when>
                                        <c:otherwise><span class="text-muted">Indéterminé</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td><fmt:formatNumber value="${c.salaire}" pattern="#,##0"/> FCFA</td>
                                <td>
                                    <c:if test="${not empty c.dateFin && c.typeContrat == 'CDD'}">
                                        <span class="badge bg-danger">
                                            <i class="fa-solid fa-clock me-1"></i>Vérifier
                                        </span>
                                    </c:if>
                                    <c:if test="${empty c.dateFin || c.typeContrat == 'CDI'}">
                                        <span class="text-muted">-</span>
                                    </c:if>
                                </td>
                                <td class="text-end pe-4">
                                    <a href="${pageContext.request.contextPath}/contrat?action=pdf&id=${c.id}"
                                       class="btn btn-sm btn-outline-secondary" title="Télécharger PDF">
                                        <i class="fa-solid fa-file-pdf"></i>
                                    </a>
                                    <c:if test="${sessionScope.role == 'RH'}">
                                        <form action="${pageContext.request.contextPath}/contrat" method="post" class="d-inline"
                                              onsubmit="return confirm('Supprimer ce contrat ?')">
                                            <input type="hidden" name="action" value="supprimer">
                                            <input type="hidden" name="id" value="${c.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger" title="Supprimer">
                                                <i class="fa-solid fa-trash"></i>
                                            </button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty contrats}">
                            <tr>
                                <td colspan="7" class="text-center py-5 text-muted">
                                    <i class="fa-solid fa-file-contract fa-3x mb-3 opacity-25 d-block"></i>
                                    Aucun contrat trouvé.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
