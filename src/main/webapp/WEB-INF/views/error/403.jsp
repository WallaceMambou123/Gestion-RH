<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>403 — Accès Interdit | Gestion RH</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap" rel="stylesheet">
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Inter', sans-serif; background: #f1f5f9; display: flex; align-items: center;
               justify-content: center; min-height: 100vh; }
        .container { text-align: center; padding: 60px 40px; background: white;
                     border-radius: 16px; box-shadow: 0 4px 24px rgba(0,0,0,.08); max-width: 450px; }
        .code { font-size: 6rem; font-weight: 700; color: #ef4444; line-height: 1; }
        .title { font-size: 1.4rem; font-weight: 700; color: #1e293b; margin: 12px 0 8px; }
        .desc  { color: #64748b; font-size: .95rem; margin-bottom: 28px; }
        .btn   { display: inline-block; padding: 10px 28px; background: #3b82f6; color: white;
                 border-radius: 8px; text-decoration: none; font-weight: 600; }
        .btn:hover { background: #2563eb; }
    </style>
</head>
<body>
    <div class="container">
        <div class="code">403</div>
        <div class="title">Accès Interdit</div>
        <p class="desc">Vous n'avez pas les permissions nécessaires pour accéder à cette page. Contactez votre administrateur RH.</p>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn">Retour au Tableau de Bord</a>
    </div>
</body>
</html>
