<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %> <%-- Для JSTL --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %> <%-- Для CSRF --%>
<html>
<head>
    <title>Вход</title>
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
            min-width: 350px; /* Adjust as needed */
        }
    </style>
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-4 card p-4 shadow">
            <h2 class="text-center mb-4">Вход</h2>

            <c:if test="${param.error != null}">
                <div class="alert alert-danger" role="alert">
                    Неверное имя пользователя или пароль!
                </div>
            </c:if>
            <c:if test="${param.logout != null}">
                <div class="alert alert-success" role="alert">
                    Вы успешно вышли из системы.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/authenticateTheUser" method="post">
                <sec:csrfInput /> <%-- Обязательно для Spring Security! --%>
                <div class="mb-3">
                    <label for="username" class="form-label">Имя пользователя:</label>
                    <input type="text" name="username" id="username" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Пароль:</label>
                    <input type="password" name="password" id="password" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary w-100 mb-2">Войти</button>
            </form>
            <p class="mt-3 text-center">Нет аккаунта? <a href="${pageContext.request.contextPath}/register">Регистрация</a></p>
        </div>
    </div>
</div>
<!-- Bootstrap JS (bundle includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>