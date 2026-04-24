<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %> <%-- Использование jakarta.tags.core --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> <%-- Для CSRF --%>
<html>
<head>
    <title>Регистрация</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background-color: #f8f9fa; /* Light background for contrast */
        }
        .card {
            min-width: 350px;
        }
    </style>
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-4 card p-4 shadow border-success">
            <h2 class="text-center text-success mb-4">Регистрация</h2>

            <c:if test="${not empty errorMessage}"> <%-- Проверяем flash-атрибут --%>
                <div class="alert alert-danger mt-3">
                        ${errorMessage}
                </div>
            </c:if>
            <c:if test="${not empty successMessage}"> <%-- Проверяем flash-атрибут --%>
                <div class="alert alert-success mt-3">
                        ${successMessage}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <sec:csrfInput /> <%-- CSRF токен --%>
                <div class="mb-3">
                    <label for="name" class="form-label">Имя пользователя:</label>
                    <input type="text" name="name" id="name" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="pass" class="form-label">Пароль:</label>
                    <input type="password" name="pass" id="pass" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="mail" class="form-label">Email:</label>
                    <input type="email" name="mail" id="mail" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-success w-100 mb-2">Создать аккаунт</button>
                <a href="${pageContext.request.contextPath}/index" class="btn btn-outline-secondary w-100">Назад ко входу</a>
            </form>
        </div>
    </div>
</div>
<!-- Bootstrap JS (bundle includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>