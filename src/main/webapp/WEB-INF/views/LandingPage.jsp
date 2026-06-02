<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>RH Manager — Gestion du Personnel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/LandingPage.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">
</head>
<body>

<!-- ========== NAVBAR ========== -->
<nav class="navbar">
    <div class="nav-brand">
        <div class="nav-icon">
            <i class="ti ti-building"></i>
        </div>
        <span class="nav-name">RH Manager</span>
    </div>
    <div class="nav-links">
        <a href="#fonctionnalites" class="nav-link">Fonctionnalités</a>
        <a href="#stats" class="nav-link">À propos</a>
        <a href="#cta" class="nav-link">Contact</a>
    </div>
    <div class="nav-actions">
        <a href="${pageContext.request.contextPath}/connexion" class="btn-outline">Se connecter</a>
        <a href="${pageContext.request.contextPath}/connexion" class="btn-primary">Commencer</a>
    </div>
</nav>

<!-- ========== HERO ========== -->
<section class="hero">
    <div class="hero-left">
        <div class="hero-badge">
            <span class="badge-dot"></span>
            Plateforme RH tout-en-un
        </div>
        <h1 class="hero-title">
            Gérez votre <span class="hero-title-accent">capital humain</span><br>
            en toute simplicité
        </h1>
        <p class="hero-desc">
            Centralisez la gestion de vos employés, contrats, congés et fiches de paie.
            Générez des rapports PDF, envoyez des SMS et pilotez votre masse salariale
            en temps réel.
        </p>
        <div class="hero-btns">
            <a href="login" class="btn-hero-primary">
                <i class="ti ti-login"></i>
                Accéder à l'application
            </a>
            <a href="#fonctionnalites" class="btn-hero-outline">
                <i class="ti ti-player-play"></i>
                Voir la démo
            </a>
        </div>
        <div class="hero-stats">
            <div class="hero-stat">
                <div class="hero-stat-val">500+</div>
                <div class="hero-stat-label">Employés gérés</div>
            </div>
            <div class="hero-divider"></div>
            <div class="hero-stat">
                <div class="hero-stat-val">6</div>
                <div class="hero-stat-label">Modules intégrés</div>
            </div>
            <div class="hero-divider"></div>
            <div class="hero-stat">
                <div class="hero-stat-val">100%</div>
                <div class="hero-stat-label">Sécurisé BCrypt</div>
            </div>
        </div>
    </div>

    <div class="hero-right">
       <img src="${pageContext.request.contextPath}/images/landing.jpg" class="imageLanding" width="500px" height="500px">
    </div>
</section>

<!-- ========== FONCTIONNALITÉS ========== -->
<section class="features" id="fonctionnalites">
    <div class="section-label">Fonctionnalités</div>
    <h2 class="section-title">Tout ce dont vous avez besoin</h2>
    <p class="section-sub">Six modules complets pour une gestion RH sans friction</p>
    <div class="features-grid">
        <div class="feat-card">
            <div class="feat-icon" style="background:#E6F1FB;color:#185FA5">
                <i class="ti ti-users"></i>
            </div>
            <div class="feat-title">Gestion des employés</div>
            <div class="feat-desc">CRUD complet, upload photo, pagination et export CSV par département.</div>
        </div>
        <div class="feat-card">
            <div class="feat-icon" style="background:#EAF3DE;color:#3B6D11">
                <i class="ti ti-file-text"></i>
            </div>
            <div class="feat-title">Contrats PDF</div>
            <div class="feat-desc">Génération automatique avec iText 7. Alertes d'expiration CDD par SMS.</div>
        </div>
        <div class="feat-card">
            <div class="feat-icon" style="background:#FAEEDA;color:#854F0B">
                <i class="ti ti-calendar-off"></i>
            </div>
            <div class="feat-title">Gestion des congés</div>
            <div class="feat-desc">Workflow complet : demande → approbation. Calcul automatique des jours.</div>
        </div>
        <div class="feat-card">
            <div class="feat-icon" style="background:#EEEDFE;color:#534AB7">
                <i class="ti ti-receipt"></i>
            </div>
            <div class="feat-title">Fiches de paie</div>
            <div class="feat-desc">Calcul automatique brut/net. PDF généré et notifié par SMS chaque mois.</div>
        </div>
        <div class="feat-card">
            <div class="feat-icon" style="background:#E1F5EE;color:#0F6E56">
                <i class="ti ti-chart-bar"></i>
            </div>
            <div class="feat-title">Statistiques RH</div>
            <div class="feat-desc">Masse salariale, taux d'absentéisme et répartition par type de contrat.</div>
        </div>
        <div class="feat-card">
            <div class="feat-icon" style="background:#FCEBEB;color:#A32D2D">
                <i class="ti ti-shield-check"></i>
            </div>
            <div class="feat-title">Sécurité BCrypt</div>
            <div class="feat-desc">Authentification sécurisée, filtres de session et rôles (RH, Manager, Employé).</div>
        </div>
    </div>
</section>

<!-- ========== BANDE STATS ========== -->
<div class="stats-band" id="stats">
    <div class="band-stat">
        <div class="band-val">6</div>
        <div class="band-label">Tables MySQL</div>
    </div>
    <div class="band-stat">
        <div class="band-val">iText 7</div>
        <div class="band-label">Génération PDF</div>
    </div>
    <div class="band-stat">
        <div class="band-val">SMSLib</div>
        <div class="band-label">Notifications SMS</div>
    </div>
    <div class="band-stat">
        <div class="band-val">BCrypt</div>
        <div class="band-label">Sécurité des mots de passe</div>
    </div>
</div>

<!-- ========== CTA ========== -->
<section class="cta" id="cta">
    <div class="cta-left">
        <div class="cta-title">Prêt à commencer ?</div>
        <div class="cta-sub">Connectez-vous à votre espace RH et prenez le contrôle de votre personnel.</div>
    </div>
    <div class="cta-btns">
        <a href="${pageContext.request.contextPath}/connexion" class="btn-hero-outline">
            <i class="ti ti-login"></i>
            Se connecter
        </a>
    </div>
</section>



</body>
</html>
