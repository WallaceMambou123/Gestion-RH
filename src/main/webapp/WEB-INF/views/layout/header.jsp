<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion RH - ${param.title}</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts: Inter -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    
    <style>
        :root {
            --sidebar-bg: #0f172a;
            --sidebar-active: #3b82f6;
            --sidebar-hover: rgba(255, 255, 255, 0.05);
            --content-bg: #f1f5f9;
            --accent: #3b82f6;
            --text-main: #1e293b;
            --text-muted: #64748b;
        }

        body { 
            font-family: 'Inter', sans-serif; 
            background-color: var(--content-bg); 
            color: var(--text-main);
        }

        /* Sidebar Styles */
        .sidebar { 
            background: var(--sidebar-bg); 
            color: white; 
            min-height: 100vh; 
            width: 280px; 
            position: fixed; 
            transition: all 0.3s ease;
            box-shadow: 4px 0 10px rgba(0,0,0,0.1);
            z-index: 1000;
        }

        .sidebar-brand {
            padding: 24px;
            display: flex;
            align-items: center;
            gap: 12px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.05);
        }

        .brand-icon {
            width: 40px;
            height: 40px;
            background: var(--accent);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
        }

        .brand-text {
            display: flex;
            flex-direction: column;
        }

        .brand-name { font-weight: 700; font-size: 18px; letter-spacing: -0.5px; }
        .brand-sub { font-size: 11px; opacity: 0.5; text-transform: uppercase; letter-spacing: 1px; }

        .sidebar-nav { padding: 20px 0; }
        .nav-section {
            padding: 10px 24px;
            font-size: 11px;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 1.5px;
            color: var(--text-muted);
            margin-top: 15px;
        }

        .nav-item { 
            color: rgba(255,255,255,0.6); 
            text-decoration: none; 
            padding: 12px 24px; 
            display: flex;
            align-items: center;
            gap: 12px;
            transition: 0.2s;
            position: relative;
            font-weight: 500;
        }

        .nav-item i { font-size: 18px; width: 24px; text-align: center; }

        .nav-item:hover { 
            background: var(--sidebar-hover); 
            color: white; 
        }

        .nav-item.active { 
            background: rgba(59, 130, 246, 0.1);
            color: var(--sidebar-active);
            border-right: 3px solid var(--sidebar-active);
        }

        .nav-item.active i { color: var(--sidebar-active); }

        .nav-badge {
            margin-left: auto;
            background: var(--accent);
            color: white;
            padding: 2px 8px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
        }

        /* Sidebar Footer */
        .sidebar-footer {
            position: absolute;
            bottom: 0;
            width: 100%;
            padding: 20px;
            background: rgba(0,0,0,0.2);
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .user-avatar {
            width: 38px;
            height: 38px;
            background: #475569;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 14px;
        }

        .user-name { font-size: 14px; font-weight: 600; }
        .user-role { font-size: 11px; opacity: 0.5; }

        .logout-btn {
            margin-left: auto;
            color: rgba(255,255,255,0.4);
            transition: 0.2s;
        }
        .logout-btn:hover { color: #ef4444; }

        /* Content Styles */
        .main-content { 
            margin-left: 280px; 
            padding: 40px; 
            width: calc(100% - 280px);
        }

        .topbar { 
            background: white; 
            padding: 20px 40px; 
            box-shadow: 0 1px 3px rgba(0,0,0,0.05); 
            margin-bottom: 40px;
            border-radius: 0 0 16px 16px;
        }

        .card { 
            border: none; 
            box-shadow: 0 10px 15px -3px rgba(0,0,0,0.04); 
            border-radius: 16px; 
            background: white;
        }

        .table thead th {
            background: #f8fafc;
            text-transform: uppercase;
            font-size: 11px;
            letter-spacing: 0.5px;
            font-weight: 700;
            color: var(--text-muted);
            padding: 16px;
            border-bottom: 1px solid #f1f5f9;
        }

        .table td { padding: 16px; color: var(--text-main); }
        .badge { font-weight: 600; padding: 6px 12px; border-radius: 8px; }

        .btn-primary { 
            background: var(--accent); 
            border: none; 
            padding: 10px 20px;
            border-radius: 10px;
            font-weight: 600;
            box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
        }

        .btn-light {
            background: #f8fafc;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            color: var(--text-main);
        }
    </style>
</head>
<body>
    <div class="d-flex">
