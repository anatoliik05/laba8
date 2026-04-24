<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Доступ запрещен</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background-color: #f8f9fa;
        }
        .card {
            min-width: 400px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6 text-center card p-4 shadow bg-warning-subtle border-warning">
            <h1 class="text-danger">Доступ запрещен!</h1>
            <p class="lead">У вас нет прав для доступа к этому ресурсу.</p>
            <hr>
            <p>
                Пожалуйста, войдите с учетной записью, имеющей необходимые разрешения,
                или свяжитесь с администратором, если считаете, что это ошибка.
            </p>
            <a href="${pageContext.request.contextPath}/" class="btn btn-warning mt-3">Вернуться на главную</a>
            <c:if test="${pageContext.request.userPrincipal != null}">
                <form action="${pageContext.request.contextPath}/logout" method="post" class="mt-2">
                    <sec:csrfInput />
                    <button type="submit" class="btn btn-outline-danger">Выйти</button>
                </form>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>