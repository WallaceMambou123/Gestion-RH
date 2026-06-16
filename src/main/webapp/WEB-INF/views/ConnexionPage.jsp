<%--
  Created by IntelliJ IDEA.
  User: Namikaze
  Date: 30/05/2026
  Time: 17:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Connexion — RH Manager</title>
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
        <h2 class="overlay-title">Gérez votre personnel en toute simplicité</h2>
        <p class="overlay-desc">Plateforme complète : employés, contrats, congés et fiches de paie.</p>
        <div class="overlay-features">
          <div class="overlay-feat"><i class="ti ti-check"></i> Gestion des employés</div>
          <div class="overlay-feat"><i class="ti ti-check"></i> Contrats PDF automatiques</div>
          <div class="overlay-feat"><i class="ti ti-check"></i> Fiches de paie par SMS</div>
          <div class="overlay-feat"><i class="ti ti-check"></i> Statistiques en temps réel</div>
        </div>
      </div>
    </div>
  </div>

  <!-- ===== COTE DROIT : FORMULAIRE ===== -->
  <div class="cote2">

    <div class="form-header">
      <div class="form-logo">
        <i class="ti ti-building"></i>
      </div>
      <h1 class="form-title">Bon retour !</h1>
      <p class="form-sub">Connectez-vous à votre espace RH</p>
    </div>

    <%-- Message d'erreur venant du Servlet --%>
    <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-error">
      <i class="ti ti-alert-circle"></i>
      <span><%= request.getAttribute("error") %></span>
    </div>
    <% } %>

    <%-- Message succès après inscription --%>
    <% if ("1".equals(request.getParameter("registered"))) { %>
    <div class="alert alert-success">
      <i class="ti ti-circle-check"></i>
      <span>
        <% if ("true".equals(request.getParameter("pending"))) { %>
          Inscription réussie ! Votre compte est en attente de validation par le RH.
        <% } else { %>
          Compte créé avec succès. Connectez-vous !
        <% } %>
      </span>
    </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/connexion" method="post" class="auth-form">

      <div class="form-group">
        <label class="form-label" for="username">Nom d'utilisateur</label>
        <div class="input-wrap">
          <i class="ti ti-user input-icon"></i>
          <input
                  class="form-input"
                  type="text"
                  id="username"
                  name="username"
                  placeholder="username"
                  value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"
                  autocomplete="username"
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
                  autocomplete="current-password"
                  required
          />
          <button type="button" class="eye-btn" onclick="togglePwd()">
            <i class="ti ti-eye" id="eye-icon"></i>
          </button>
        </div>
      </div>

      <div class="form-options">
        <label class="checkbox-label">
          <input type="checkbox" name="remember" value="true">
          Se souvenir de moi
        </label>
        <a href="#" class="forgot-link">Mot de passe oublié ?</a>
      </div>

      <button type="submit" class="btn-submit">
        <i class="ti ti-login"></i>
        Se connecter
      </button>

    </form>

    <div class="form-footer">
      Pas encore de compte ?
      <a href="${pageContext.request.contextPath}/inscription" class="form-link">Créer un compte</a>
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
