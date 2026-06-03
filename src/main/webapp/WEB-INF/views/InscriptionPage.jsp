<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription — RH Manager</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ConnexionPage.css">
</head>
<body>

<div class="contenair">

    <!-- ===== COTE GAUCHE : IMAGE ===== -->
    <div class="cote1">
        <img src="${pageContext.request.contextPath}/images/login.jpg" alt="RH Manager">
        <div class="cote1-overlay">
            <div class="overlay-brand">
                <div class="brand-icon"><i class="ti ti-building"></i></div>
                <span class="brand-name">RH Manager</span>
            </div>
            <div class="overlay-body">
                <h2 class="overlay-title">Rejoignez notre plateforme de gestion RH</h2>
                <p class="overlay-desc">Un outil puissant pour simplifier votre quotidien administratif.</p>
                <div class="overlay-features">
                    <div class="overlay-feat"><i class="ti ti-check"></i> Inscription rapide</div>
                    <div class="overlay-feat"><i class="ti ti-check"></i> Accès sécurisé</div>
                    <div class="overlay-feat"><i class="ti ti-check"></i> Interface intuitive</div>
                </div>
            </div>
        </div>
    </div>

    <!-- ===== COTE DROIT : FORMULAIRE ===== -->
    <div class="cote2">

        <div class="form-header">
            <div class="form-logo">
                <i class="ti ti-user-plus"></i>
            </div>
            <h1 class="form-title">Créer un compte</h1>
            <p class="form-sub">Remplissez les informations ci-dessous</p>
        </div>

        <%-- Message d'erreur venant du Servlet --%>
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error">
            <i class="ti ti-alert-circle"></i>
            <span><%= request.getAttribute("error") %></span>
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/inscription" method="post" class="auth-form">

            <!-- Nom & Prénom -->
            <div class="form-row-double">
                <div class="form-group">
                    <label class="form-label" for="nom">Nom</label>
                    <div class="input-wrap">
                        <i class="ti ti-user input-icon"></i>
                        <input
                                class="form-input"
                                type="text"
                                id="nom"
                                name="nom"
                                placeholder="Doe"
                                value="<%= request.getParameter("nom") != null ? request.getParameter("nom") : "" %>"
                                required
                        />
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label" for="prenom">Prénom</label>
                    <div class="input-wrap">
                        <i class="ti ti-user input-icon"></i>
                        <input
                                class="form-input"
                                type="text"
                                id="prenom"
                                name="prenom"
                                placeholder="John"
                                value="<%= request.getParameter("prenom") != null ? request.getParameter("prenom") : "" %>"
                                required
                        />
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="username">Nom d'utilisateur</label>
                <div class="input-wrap">
                    <i class="ti ti-at input-icon"></i>
                    <input
                            class="form-input"
                            type="text"
                            id="username"
                            name="username"
                            placeholder="johndoe"
                            value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"
                            required
                    />
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="email">Email professionnel</label>
                <div class="input-wrap">
                    <i class="ti ti-mail input-icon"></i>
                    <input
                            class="form-input"
                            type="email"
                            id="email"
                            name="email"
                            placeholder="john.doe@entreprise.com"
                            value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
                            required
                    />
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="mdp">Mot de passe</label>
                <div class="input-wrap">
                    <i class="ti ti-lock input-icon"></i>
                    <input
                            class="form-input"
                            type="password"
                            id="mdp"
                            name="mdp"
                            placeholder="••••••••"
                            required
                    />
                    <button type="button" class="eye-btn" onclick="togglePwd()">
                        <i class="ti ti-eye" id="eye-icon"></i>
                    </button>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label" for="role">Rôle</label>
                <div class="input-wrap">
                    <i class="ti ti-users input-icon"></i>
                    <select name="role" id="role" class="form-input form-select" required>
                        <option value="" disabled selected>Choisir un rôle...</option>
                        <option value="RH" <%= "RH".equals(request.getParameter("role")) ? "selected" : "" %>>RH</option>
                        <option value="MANAGER" <%= "MANAGER".equals(request.getParameter("role")) ? "selected" : "" %>>Manager</option>
                        <option value="EMPLOYE" <%= "EMPLOYE".equals(request.getParameter("role")) ? "selected" : "" %>>Employé</option>
                    </select>
                </div>
            </div>

            <button type="submit" class="btn-submit">
                <i class="ti ti-user-plus"></i>
                S'inscrire
            </button>

        </form>

        <div class="form-footer">
            Déjà un compte ?
            <a href="${pageContext.request.contextPath}/connexion" class="form-link">Se connecter</a>
        </div>

    </div>
</div>

<script>
    function togglePwd() {
        const input = document.getElementById('mdp');
        const icon  = document.getElementById('eye-icon');
        if (input.type === 'password') {
            input.type = 'text';
            icon.className = 'ti ti-eye-off';
        } else {
            input.type = 'password';
            icon.className = 'ti ti-eye';
        }
    }
</script>

</body>
</html>
