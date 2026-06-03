<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="${not empty employe ? 'Modifier Employé' : 'Nouvel Employé'}" />
</jsp:include>
<jsp:include page="/WEB-INF/views/layout/sidebar.jsp" />

<main class="main-content flex-grow-1">
    <div class="topbar">
        <h5 class="m-0 fw-bold text-secondary">
            <a href="${pageContext.request.contextPath}/employe" class="text-decoration-none me-2"><i class="fa-solid fa-arrow-left"></i></a>
            ${not empty employe ? 'Modification de fiche' : 'Enregistrement employé'}
        </h5>
    </div>

    <div class="container-fluid">
        <div class="card p-4">
            <form action="${pageContext.request.contextPath}/employe" method="post" enctype="multipart/form-data">
                <input type="hidden" name="id" value="${employe.id}">
                
                <div class="row g-4">
                    <div class="col-md-4 text-center border-end">
                        <div class="mb-3">
                            <label class="form-label d-block text-muted text-uppercase fw-bold small">Photo d'identité</label>
                            <img src="${not empty employe.photoFilename ? pageContext.request.contextPath.concat('/uploads/photos/').concat(employe.photoFilename) : 'https://ui-avatars.com/api/?name=User&size=150&background=f1f5f9'}" 
                                 class="rounded-circle mb-3 border shadow-sm" width="150" height="150" id="previewImg">
                            <input type="file" name="photo" class="form-control form-control-sm" accept="image/*" onchange="previewFile(this)">
                        </div>
                    </div>
                    
                    <div class="col-md-8">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Matricule</label>
                                <input type="text" name="matricule" class="form-control" value="${employe.matricule}" required ${not empty employe ? 'readonly' : ''}>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Poste Occupé</label>
                                <input type="text" name="poste" class="form-control" value="${employe.poste}" required placeholder="ex: Analyste Développeur">
                            </div>
                            
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Nom</label>
                                <input type="text" name="nom" class="form-control" value="${employe.nom}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Prénom</label>
                                <input type="text" name="prenom" class="form-control" value="${employe.prenom}" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold">E-mail Professionnel</label>
                                <input type="email" name="email" class="form-control" value="${employe.email}" required placeholder="user@entreprise.com">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Téléphone</label>
                                <input type="text" name="telephone" class="form-control" value="${employe.telephone}" placeholder="+237 ...">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold">Département</label>
                                <select name="departementId" class="form-select" required>
                                    <option value="">Sélectionner...</option>
                                    <c:forEach var="d" items="${departements}">
                                        <option value="${d.id}" ${employe.departement.id == d.id ? 'selected' : ''}>${d.nom}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Type de Contrat</label>
                                <select name="typeContrat" class="form-select" required>
                                    <option value="CDI" ${employe.typeContrat == 'CDI' ? 'selected' : ''}>CDI</option>
                                    <option value="CDD" ${employe.typeContrat == 'CDD' ? 'selected' : ''}>CDD</option>
                                    <option value="STAGE" ${employe.typeContrat == 'STAGE' ? 'selected' : ''}>STAGE</option>
                                    <option value="CONSULTANT" ${employe.typeContrat == 'CONSULTANT' ? 'selected' : ''}>CONSULTANT</option>
                                </select>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold">Date d'embauche</label>
                                <input type="date" name="dateEmbauche" class="form-control" value="${employe.dateEmbauche}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Salaire de BASE (FCFA)</label>
                                <input type="number" name="salaireBase" class="form-control" value="${employe.salaireBase}" required step="0.01">
                            </div>
                        </div>
                        
                        <div class="mt-5 pt-3 border-top d-flex justify-content-end gap-2">
                            <a href="${pageContext.request.contextPath}/employe" class="btn btn-light border px-4">Annuler</a>
                            <button type="submit" class="btn btn-primary px-4 fw-bold">Enregistrer le profil</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</main>

<script>
    function previewFile(input) {
        var file = input.files[0];
        if (file) {
            var reader = new FileReader();
            reader.onload = function() {
                document.getElementById("previewImg").src = reader.result;
            }
            reader.readAsDataURL(file);
        }
    }
</script>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
