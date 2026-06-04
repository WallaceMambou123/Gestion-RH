<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Générer une Fiche de Paie"/>
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp">
    <jsp:param name="page" value="paie"/>
</jsp:include>

<main class="main-content">
    <div class="page-header d-flex align-items-center mb-4">
        <a href="${pageContext.request.contextPath}/paie?action=liste" class="btn btn-ghost btn-icon me-3">
            <i class="ti ti-arrow-left fs-3"></i>
        </a>
        <div>
            <h2 class="page-title">Calcul de Paie</h2>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/paie?action=liste">Paie</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Génération</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row mt-4">
        <div class="col-lg-8">
            <div class="card p-lg-4 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title fw-bold mb-4 border-bottom pb-3">Éléments du Bulletin</h5>

                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger d-flex align-items-center" role="alert">
                            <i class="ti ti-alert-triangle me-2 fs-4"></i>
                            <div>${requestScope.error}</div>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/paie" method="post" id="paieForm" class="row g-4">
                        <input type="hidden" name="action" value="generer">

                        <div class="col-md-8">
                            <label class="form-label fw-semibold">Collaborateur <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="ti ti-user-check"></i></span>
                                <select name="employeId" class="form-select" required id="employeSelect">
                                    <option value="">Choisir un employé...</option>
                                    <c:forEach var="emp" items="${employes}">
                                        <option value="${emp.id}"
                                                data-salaire="${emp.salaireBase}"
                                                ${param.employeId == emp.id ? 'selected' : ''}>
                                            ${emp.matricule} — ${emp.prenom} ${emp.nom}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Mois de paie <span class="text-danger">*</span></label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="ti ti-calendar"></i></span>
                                <input type="month" name="mois" class="form-control" required
                                       value="${not empty param.mois ? param.mois : ''}">
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Salaire de Base</label>
                            <div class="input-group input-group-flat">
                                <input type="number" id="salaireBase" class="form-control bg-light"
                                       step="1" readonly value="0">
                                <span class="input-group-text bg-light text-muted small">FCFA</span>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Heures Supplémentaires</label>
                            <div class="input-group">
                                <input type="number" name="heuresSup" id="heuresSup" class="form-control"
                                       step="0.5" min="0" value="0" oninput="calculer()">
                                <span class="input-group-text text-muted small">heures</span>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Primes Diverses</label>
                            <div class="input-group">
                                <input type="number" name="primes" id="primes" class="form-control"
                                       step="100" min="0" value="0" oninput="calculer()">
                                <span class="input-group-text text-muted small">FCFA</span>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Retenues / Cotisations</label>
                            <div class="input-group">
                                <input type="number" name="retenues" id="retenues" class="form-control"
                                       step="100" min="0" value="0" oninput="calculer()">
                                <span class="input-group-text text-danger border-danger border-opacity-25 bg-danger bg-opacity-5 small">- FCFA</span>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Montant H. Supp (1.25)</label>
                            <input type="text" id="montantHS" class="form-control bg-light" readonly value="0">
                        </div>

                        <div class="col-md-4">
                            <label class="form-label fw-semibold">Total Brut</label>
                            <input type="text" id="salaireBrut" class="form-control bg-light fw-bold" readonly value="0">
                        </div>

                        <div class="col-12 pt-4 border-top">
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="bg-primary bg-opacity-10 text-primary p-3 rounded-pill px-4">
                                    <span class="small fw-bold text-uppercase opacity-75">Net à Verser :</span>
                                    <h4 class="d-inline mb-0 ms-2 fw-bold" id="salaireNet">0 FCFA</h4>
                                </div>
                                <div class="btn-group-actions">
                                    <a href="${pageContext.request.contextPath}/paie?action=liste" class="btn btn-light px-4 me-2 border">Annuler</a>
                                    <button type="submit" class="btn btn-primary px-5 fw-bold shadow-sm">
                                        <i class="ti ti-calculator me-2"></i>Valider et Enregistrer
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="card bg-light border-0 p-3 h-100">
                <div class="card-body">
                    <h6 class="fw-bold mb-3 d-flex align-items-center">
                        <i class="ti ti-help-circle-filled me-2 text-primary"></i> Aide au calcul
                    </h6>
                    <p class="small text-muted">
                        Le taux horaire est calculé sur une base légale de <strong>173 heures</strong> par mois.
                    </p>
                    <div class="p-3 bg-white rounded border mb-3">
                        <code class="d-block small text-dark">H.Supp = (Base / 173) × 1.25</code>
                    </div>
                    <hr>
                    <h6 class="fw-bold mb-2 small">Déductions automatiques</h6>
                    <ul class="small text-muted ps-3">
                        <li>Les retenues incluent l'IRPP et les cotisations sociales.</li>
                        <li>Assurez-vous de bien déduire les acomptes si nécessaire.</li>
                    </ul>

                    <div class="alert alert-warning mt-4 p-3 border-0 rounded-4">
                        <div class="d-flex">
                            <i class="ti ti-bell-ringing fs-4 me-2"></i>
                            <div>
                                <h6 class="fw-bold mb-1 small">Alerte SMS</h6>
                                <p class="small mb-0 opacity-75">Un SMS de notification sera envoyé au collaborateur après validation.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
function fmt(n) {
    return new Intl.NumberFormat('fr-FR').format(Math.round(n));
}

document.getElementById('employeSelect').addEventListener('change', function() {
    const opt = this.options[this.selectedIndex];
    const sal = parseFloat(opt.dataset.salaire) || 0;
    document.getElementById('salaireBase').value = sal;
    calculer();
});

function calculer() {
    const sal  = parseFloat(document.getElementById('salaireBase').value) || 0;
    const hs   = parseFloat(document.getElementById('heuresSup').value)   || 0;
    const pri  = parseFloat(document.getElementById('primes').value)       || 0;
    const ret  = parseFloat(document.getElementById('retenues').value)     || 0;

    const tauxHoraire = sal / 173;
    const montantHS   = hs * tauxHoraire * 1.25;
    const brut        = sal + montantHS + pri;
    const net         = brut - ret;

    document.getElementById('montantHS').value   = fmt(montantHS) + ' F';
    document.getElementById('salaireBrut').value = fmt(brut) + ' F';
    document.getElementById('salaireNet').textContent = fmt(net) + ' FCFA';
}
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
