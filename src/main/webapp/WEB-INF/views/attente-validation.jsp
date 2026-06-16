<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>En attente de validation — RH Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">
    <style>
        body { background: #f8fafc; font-family: 'Inter', sans-serif; display: flex; align-items: center; justify-content: center; height: 100vh; margin: 0; }
        .card { max-width: 500px; padding: 40px; border-radius: 16px; border: none; box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1); text-align: center; }
        .icon { width: 80px; height: 80px; background: #eff6ff; color: #2563eb; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 24px; font-size: 40px; }
        h1 { font-size: 24px; font-weight: 700; color: #1e293b; margin-bottom: 12px; }
        p { color: #64748b; line-height: 1.6; }
        .btn { border-radius: 8px; font-weight: 600; padding: 12px 24px; margin-top: 24px; }
    </style>
</head>
<body>

<div class="card">
    <div class="icon">
        <i class="ti ti-clock-hour-4"></i>
    </div>
    <h1>Inscription enregistrée !</h1>
    <p>Bonjour. Votre demande de création de compte <strong>Manager</strong> a bien été reçue.</p>
    <p>Pour des raisons de sécurité, un Administrateur RH doit valider votre poste. Ce processus peut prendre jusqu'à <strong>72 heures</strong>.</p>
    <p>Vous recevrez un email dès que votre accès sera activé.</p>
    
    <a href="${pageContext.request.contextPath}/connexion" class="btn btn-primary w-100">Retour à l'accueil</a>
</div>

</body>
</html>
