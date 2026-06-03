<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Approuver Congé" />
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />

<main class="main-content flex-grow-1">
    <div class="topbar border-bottom mb-4">
        <h5 class="fw-bold text-secondary">Validation de la Demande de Conge</h5>
    </div>

    <div class="container-fluid">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card p-4">
                    <div class="mb-4 text-center">
                        <img src="https://ui-avatars.com/api/?name=${conge.employe.prenom}+${conge.employe.nom}&background=3b82f6&color=fff" class="rounded-circle mb-3" width="80">
                        <h4 class="fw-bold">${conge.employe.prenom} ${conge.employe.nom}</h4>
                        <span class="badge bg-secondary">${conge.employe.poste} - ${conge.employe.departement.nom}</span>
                    </div>

                    <div class="border rounded p-3 mb-4 bg-light">
                        <div class="row">
                            <div class="col-6 mb-2">
                                <small class="text-muted d-block">Type</small>
                                <strong>${conge.typeConge}</strong>
                            </div>
                            <div class="col-6 mb-2">
                                <small class="text-muted d-block">Statut</small>
                                <span class="badge bg-warning">${conge.statut}</span>
                            </div>
                            <div class="col-6">
                                <small class="text-muted d-block">Du</small>
                                <strong>${conge.dateDebut}</strong>
                            </div>
                            <div class="col-6">
                                <small class="text-muted d-block">Au</small>
                                <strong>${conge.dateFin}</strong>
                            </div>
                        </div>
                        <div class="mt-3">
                            <small class="text-muted d-block">Motif</small>
                            <p class="mb-0">${conge.motif != null ? conge.motif : "Aucun motif fourni."}</p>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/conge" method="post">
                        <input type="hidden" name="action" value="validate">
                        <input type="hidden" name="id" value="${conge.id}">
                        
                        <div class="mb-3">
                            <label class="form-label">Ma décision</label>
                            <select name="decision" class="form-select" id="decisionSelect" onchange="toggleMotif()">
                                <option value="APPROUVE">Approuver la demande</option>
                                <option value="REFUSE">Refuser la demande</option>
                            </select>
                        </div>

                        <div class="mb-3" id="motifRefusGroup" style="display: none;">
                            <label class="form-label">Motif du refus</label>
                            <textarea name="motifRefus" class="form-input" rows="3"></textarea>
                        </div>

                        <div class="d-flex gap-2 justify-content-end mt-4">
                            <a href="${pageContext.request.contextPath}/conge" class="btn btn-light border">Annuler</a>
                            <button type="submit" class="btn btn-primary px-4">Enregistrer la decision</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    function toggleMotif() {
        const select = document.getElementById('decisionSelect');
        const group = document.getElementById('motifRefusGroup');
        group.style.display = select.value === 'REFUSE' ? 'block' : 'none';
    }
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
