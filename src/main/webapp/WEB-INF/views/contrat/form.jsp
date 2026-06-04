<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Nouveau Contrat"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp"/>

<main class="main-content flex-grow-1">
    <div class="topbar">
        <h5 class="m-0 fw-bold text-secondary">
            <a href="${pageContext.request.contextPath}/contrat" class="text-decoration-none me-2">
                <i class="fa-solid fa-arrow-left"></i>
            </a>
            ${not empty contrat ? 'Modifier le contrat' : 'Nouveau contrat'}
        </h5>
    </div>

    <div class="container-fluid">
        <div class="row justify-content-center">
            <div class="col-lg-7">
                <div class="card p-4">
                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger"><i class="fa-solid fa-circle-xmark me-2"></i>${requestScope.error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/contrat" method="post">
                        <input type="hidden" name="action" value="sauvegarder">
                        <input type="hidden" name="id" value="${contrat.id}">

                        <div class="mb-3">
                            <label class="form-label fw-bold">Employé <span class="text-danger">*</span></label>
                            <select name="employeId" class="form-select" required>
                                <option value="">Sélectionner un employé...</option>
                                <c:forEach var="emp" items="${employes}">
                                    <option value="${emp.id}" ${contrat.employe.id == emp.id ? 'selected' : ''}>
                                        ${emp.matricule} — ${emp.prenom} ${emp.nom}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Type de Contrat <span class="text-danger">*</span></label>
                                <select name="typeContrat" class="form-select" required id="typeContratSelect">
                                    <option value="CDI"   ${contrat.typeContrat == 'CDI'   ? 'selected' : ''}>CDI</option>
                                    <option value="CDD"   ${contrat.typeContrat == 'CDD'   ? 'selected' : ''}>CDD</option>
                                    <option value="STAGE" ${contrat.typeContrat == 'STAGE' ? 'selected' : ''}>STAGE</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Salaire (FCFA) <span class="text-danger">*</span></label>
                                <input type="number" name="salaire" class="form-control"
                                       value="${contrat.salaire}" required step="1000" min="0">
                            </div>
                        </div>

                        <div class="row g-3 mt-1">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Date de début <span class="text-danger">*</span></label>
                                <input type="date" name="dateDebut" class="form-control"
                                       value="${contrat.dateDebut}" required>
                            </div>
                            <div class="col-md-6" id="dateFinBlock">
                                <label class="form-label fw-bold">Date de fin <small class="text-muted">(CDD/STAGE)</small></label>
                                <input type="date" name="dateFin" class="form-control"
                                       value="${contrat.dateFin}">
                            </div>
                        </div>

                        <div class="mb-3 mt-3">
                            <label class="form-label fw-bold">Avantages</label>
                            <textarea name="avantages" class="form-control" rows="3"
                                      placeholder="ex: Mutuelle, tickets restaurant, véhicule de service...">${contrat.avantages}</textarea>
                        </div>

                        <div class="d-flex justify-content-end gap-2 pt-3 border-top">
                            <a href="${pageContext.request.contextPath}/contrat" class="btn btn-light border px-4">Annuler</a>
                            <button type="submit" class="btn btn-primary px-4 fw-bold">
                                <i class="fa-solid fa-floppy-disk me-2"></i>Enregistrer
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
document.getElementById('typeContratSelect').addEventListener('change', function() {
    const dateFinBlock = document.getElementById('dateFinBlock');
    dateFinBlock.style.opacity = this.value === 'CDI' ? '0.4' : '1';
});
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
