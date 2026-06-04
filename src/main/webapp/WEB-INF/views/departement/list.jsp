<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Gestion des Départements"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
    <jsp:param name="page" value="departements"/>
</jsp:include>

<main class="main-content">
    <div class="page-header d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="page-title">Départements</h2>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Départements</li>
                </ol>
            </nav>
        </div>
        <a href="${pageContext.request.contextPath}/departement?action=formulaire" class="btn btn-primary">
            <i class="ti ti-plus me-1"></i> Nouveau département
        </a>
    </div>

    <div class="row g-4">
        <c:forEach var="dept" items="${departements}">
            <div class="col-md-6 col-lg-4">
                <div class="card card-hover h-100">
                    <div class="card-body p-4">
                        <div class="d-flex justify-content-between align-items-start mb-3">
                            <div class="icon-shape bg-secondary bg-opacity-10 text-secondary rounded">
                                <i class="ti ti-building-community fs-4"></i>
                            </div>
                            <div class="dropdown">
                                <button class="btn btn-ghost btn-icon" data-bs-toggle="dropdown">
                                    <i class="ti ti-dots-vertical"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/departement?action=formulaire&id=${dept.id}">
                                            <i class="ti ti-edit me-2"></i>Modifier
                                        </a>
                                    </li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <form action="${pageContext.request.contextPath}/departement" method="post" onsubmit="return confirm('Supprimer ce département ? Cette action est irréversible.')">
                                            <input type="hidden" name="action" value="supprimer">
                                            <input type="hidden" name="id" value="${dept.id}">
                                            <button type="submit" class="dropdown-item text-danger">
                                                <i class="ti ti-trash me-2"></i>Supprimer
                                            </button>
                                        </form>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <h4 class="card-title fw-bold mb-1">${dept.nom}</h4>
                        <p class="text-muted small mb-3">
                            <i class="ti ti-user-star me-1 text-primary"></i> Resp : ${not empty dept.responsable ? dept.responsable : 'Non assigné'}
                        </p>

                        <div class="d-grid gap-2 text-center py-3 my-3 bg-light rounded border border-light-subtle">
                            <div class="row">
                                <div class="col-6 border-end">
                                    <h5 class="mb-0 fw-bold text-dark">${not empty totalEmployesParDept[dept.id] ? totalEmployesParDept[dept.id] : 0}</h5>
                                    <span class="text-muted small">Employés</span>
                                </div>
                                <div class="col-6">
                                    <h5 class="mb-0 fw-bold text-dark">
                                        <c:choose>
                                            <c:when test="${not empty dept.budgetMasseSalariale}">
                                                <fmt:formatNumber value="${dept.budgetMasseSalariale}" pattern="#,##0"/>
                                            </c:when>
                                            <c:otherwise>0</c:otherwise>
                                        </c:choose>
                                    </h5>
                                    <span class="text-muted small">Budget (FCFA)</span>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mt-auto">
                            <a href="${pageContext.request.contextPath}/employe?action=liste&deptId=${dept.id}" class="btn btn-sm btn-outline-primary">
                                Voir l'équipe <i class="ti ti-arrow-right ms-1"></i>
                            </a>
                            <span class="badge bg-primary bg-opacity-10 text-primary">ID: ${dept.id}</span>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty departements}">
            <div class="col-12">
                <div class="card p-5 text-center shadow-none border-dashed">
                    <div class="card-body">
                        <i class="ti ti-building-community fs-1 text-muted opacity-50 mb-3"></i>
                        <h4 class="fw-bold">Aucun département trouvé</h4>
                        <p class="text-muted">Commencez par créer un département pour organiser votre personnel.</p>
                        <a href="${pageContext.request.contextPath}/departement?action=formulaire" class="btn btn-primary mt-2">
                            <i class="ti ti-plus me-1"></i> Ajouter un département
                        </a>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
