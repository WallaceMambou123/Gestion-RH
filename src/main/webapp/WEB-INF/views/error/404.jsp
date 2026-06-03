<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="title" value="Page Introuvable" />
</jsp:include>

<main class="main-content flex-grow-1 d-flex align-items-center justify-content-center" style="margin-left: 0;">
    <div class="text-center">
        <h1 class="display-1 fw-bold text-primary">404</h1>
        <h2 class="fw-bold">Oups ! Page Introuvable</h2>
        <p class="text-muted">La page que vous recherchez n'existe pas ou a été déplacée.</p>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary mt-3">Retour au Dashboard</a>
    </div>
</main>

<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
