<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Validation des comptes" />
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />

<main class="main-content flex-grow-1">
    <div class="topbar">
        <h5 class="m-0 fw-bold text-secondary">Validation des inscriptions en attente</h5>
    </div>

    <div class="container-fluid">
        <c:if test="${param.success == '1'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                L'action a été effectuée avec succès.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="bg-light">
                            <tr>
                                <th class="ps-4">Utilisateur</th>
                                <th>Email</th>
                                <th>Rôle Demandé</th>
                                <th>Date Inscription</th>
                                <th class="text-end pe-4">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${pendingUsers}">
                                <tr>
                                    <td class="ps-4">
                                        <div class="d-flex align-items-center">
                                            <div class="avatar bg-light text-primary me-2 rounded-circle d-flex align-items-center justify-content-center" style="width: 32px; height: 32px; font-weight: bold;">
                                                ${u.username.substring(0,1).toUpperCase()}
                                            </div>
                                            <div>
                                                <div class="fw-bold text-dark">${u.prenom} ${u.nom}</div>
                                                <small class="text-muted">@${u.username}</small>
                                            </div>
                                        </div>
                                    </td>
                                    <td>${u.email}</td>
                                    <td>
                                        <span class="badge ${u.role == 'MANAGER' ? 'bg-primary' : 'bg-info'}">
                                            ${u.role}
                                        </span>
                                    </td>
                                    <td class="text-muted small">${u.dateCreation}</td>
                                    <td class="text-end pe-4">
                                        <form action="${pageContext.request.contextPath}/validation" method="post" style="display: inline;">
                                            <input type="hidden" name="userId" value="${u.id}">
                                            <input type="hidden" name="action" value="approuver">
                                            <button type="submit" class="btn btn-sm btn-success px-3">
                                                <i class="fa-solid fa-check me-1"></i> Approuver
                                            </button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/validation" method="post" style="display: inline;">
                                            <input type="hidden" name="userId" value="${u.id}">
                                            <input type="hidden" name="action" value="rejeter">
                                            <button type="submit" class="btn btn-sm btn-outline-danger px-3" onclick="return confirm('Voulez-vous vraiment rejeter cette demande ?')">
                                                <i class="fa-solid fa-xmark me-1"></i> Rejeter
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty pendingUsers}">
                                <tr>
                                    <td colspan="5" class="text-center py-5 text-muted">
                                        <i class="fa-solid fa-circle-check fa-3x mb-3 d-block opacity-25"></i>
                                        Aucune demande en attente de validation.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
