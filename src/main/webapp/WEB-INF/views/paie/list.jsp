<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Fiches de Paie"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
    <jsp:param name="page" value="paie"/>
</jsp:include>

<main class="main-content">
    <div class="page-header d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="page-title">Fiches de Paie</h2>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Paie</li>
                </ol>
            </nav>
        </div>
        <c:if test="${sessionScope.role == 'RH'}">
            <a href="${pageContext.request.contextPath}/paie?action=formulaire" class="btn btn-primary">
                <i class="ti ti-plus me-1"></i> Générer une fiche
            </a>
        </c:if>
    </div>

    <%-- Filtre --%>
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-3">
            <form method="get" action="${pageContext.request.contextPath}/paie" class="row g-3 align-items-end">
                <input type="hidden" name="action" value="liste">
                <div class="col-auto">
                    <label class="form-label small fw-bold text-muted text-uppercase mb-1">Période</label>
                    <div class="input-group input-group-sm">
                        <span class="input-group-text"><i class="ti ti-calendar"></i></span>
                        <input type="month" name="mois" class="form-control" value="${param.mois}">
                    </div>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-sm btn-primary">
                        <i class="ti ti-filter me-1"></i> Filtrer
                    </button>
                    <a href="${pageContext.request.contextPath}/paie?action=liste" class="btn btn-sm btn-light border ms-1">
                        <i class="ti ti-refresh me-1"></i> Reset
                    </a>
                </div>
            </form>
        </div>
    </div>

    <div class="card shadow-sm border-0 overflow-hidden">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="bg-light">
                    <tr>
                        <th class="ps-4">Employé</th>
                        <th>Mois</th>
                        <th class="text-end">Base / Heures Sup</th>
                        <th class="text-end">Primes / Retenues</th>
                        <th class="text-end">Net à Payer</th>
                        <th class="text-end pe-4">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="fp" items="${fichesPayie}">
                        <tr>
                            <td class="ps-4">
                                <div class="d-flex align-items-center">
                                    <div class="avatar-soft bg-info rounded-circle me-3">
                                        ${fp.employe.prenom.charAt(0)}${fp.employe.nom.charAt(0)}
                                    </div>
                                    <div>
                                        <div class="fw-bold text-dark">${fp.employe.prenom} ${fp.employe.nom}</div>
                                        <code class="text-muted small">${fp.employe.matricule}</code>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <span class="badge bg-primary bg-opacity-10 text-primary px-3 text-uppercase">${fp.mois}</span>
                            </td>
                            <td class="text-end">
                                <div class="fw-semibold"><fmt:formatNumber value="${fp.salaireBase}" pattern="#,##0"/></div>
                                <small class="text-muted">${fp.heuresSup}h supp.</small>
                            </td>
                            <td class="text-end">
                                <div class="text-success small">+ <fmt:formatNumber value="${fp.primes}" pattern="#,##0"/></div>
                                <div class="text-danger small">- <fmt:formatNumber value="${fp.retenues}" pattern="#,##0"/></div>
                            </td>
                            <td class="text-end">
                                <h5 class="fw-bold text-dark mb-0">
                                    <fmt:formatNumber value="${fp.salaireNet}" pattern="#,##0"/>
                                    <small class="text-muted" style="font-size: 0.6rem;">FCFA</small>
                                </h5>
                            </td>
                            <td class="text-end pe-4">
                                <div class="btn-group">
                                    <a href="${pageContext.request.contextPath}/paie?action=pdf&id=${fp.id}"
                                       class="btn btn-icon btn-ghost-danger" title="Télécharger Bulletin PDF">
                                        <i class="ti ti-file-type-pdf fs-4"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty fichesPayie}">
                        <tr>
                            <td colspan="6" class="text-center py-5">
                                <div class="py-5">
                                    <i class="ti ti-receipt-off fs-1 text-muted opacity-25"></i>
                                    <h5 class="fw-bold mt-3 text-muted">Aucune fiche de paie</h5>
                                    <p class="text-muted small">Les bulletins générés s'afficheront ici.</p>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
