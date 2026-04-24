<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Экспериментальный Отчет</title>
    <!-- Подключение Bootstrap CSS из CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <style>
        /* Опциональные кастомные стили, если что-то не удается сделать только с Bootstrap */
        /* Например, можно изменить отступы или цвет, если стандартные Bootstrap не подходят */
        body {
            background-color: #f8f9fa; /* Светлый фон */
        }
        .logout-link {
            width: 120px; /* Фиксированная ширина для кнопки выхода */
            margin: 40px auto;
            display: block; /* Центрирование */
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h1 class="text-center mb-4">
        Добро пожаловать, <sec:authentication property="principal.name"/>!
        <br>
        <small class="text-muted fs-5">Ваша роль:
            <c:forEach var="authority" items="${pageContext.request.userPrincipal.authorities}">
                <c:if test="${authority.authority.startsWith('ROLE_')}">
                    ${authority.authority.replace("ROLE_", "")}
                </c:if>
            </c:forEach>
        </small>
    </h1>

    <sec:authorize access="hasRole('ADMIN')">
    <h2 class="mb-3">Полный отчет по экспериментам (Админ-панель)</h2>

    <!-- Кнопка для добавления нового эксперимента -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/experiments/new" class="btn btn-success">Добавить новый эксперимент</a>
    </div>
    <div class="table-responsive">
        <table class="table table-striped table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Бригада</th>
                <th>Вещество</th>
                <th>Результат</th>
                <th>Действия</th> <!-- Новый столбец для кнопок -->
            </tr>
            </thead>
            <tbody>
            <c:forEach var="exp" items="${dataList}">
                <tr>
                    <td>${exp.id}</td>
                    <td>${exp.team.teamName}</td>
                    <td>${exp.probe.substanceName}</td>
                    <td>${exp.result}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/experiments/edit/${exp.id}" class="btn btn-warning btn-sm me-2">Редактировать</a>

                        <form action="${pageContext.request.contextPath}/experiments/delete/${exp.id}" method="post" class="d-inline">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-danger btn-sm"
                                    onclick="return confirm('Вы уверены, что хотите удалить этот эксперимент?');">
                                Удалить
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="card bg-light mt-4">
        <div class="card-body">
            <h3 class="card-title">Меню администратора</h3>
            <a href="${pageContext.request.contextPath}/users/manage" class="btn btn-primary">Управление пользователями</a>
        </div>
    </div>
    </sec:authorize>

    <sec:authorize access="hasRole('USER')">
    <h2 class="mb-3">Список химических проб</h2>

    <!-- Кнопка для добавления новой пробы -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/probes/new" class="btn btn-success">Добавить новую пробу</a>
    </div>
        <div class="table-responsive">
            <table class="table table-striped table-bordered table-hover">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Название</th>
                    <th>Вес</th>
                    <th>Действия</th> <!-- Новый столбец для кнопок -->
                </tr>
                </thead>
                <tbody>
                <c:forEach var="probe" items="${dataList}">
                    <tr>
                        <td>${probe.id}</td>
                        <td>${probe.substanceName}</td>
                        <td>${probe.weight}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/probes/edit/${probe.id}" class="btn btn-warning btn-sm me-2">Редактировать</a>

                            <form action="${pageContext.request.contextPath}/probes/delete/${probe.id}" method="post" class="d-inline">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit" class="btn btn-danger btn-sm"
                                        onclick="return confirm('Вы уверены, что хотите удалить эту пробу?');">
                                    Удалить
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </sec:authorize>

    <!-- Кнопка выхода -->
    <form action="${pageContext.request.contextPath}/logout" method="post" class="text-center mt-5">
        <input type="hidden" name="csrf.parameterName" value="{csrf.token}"/>
        <button type="submit" class="btn btn-danger logout-link">Выйти</button>
    </form>
</div>

<!-- Подключение Bootstrap JS (в конце body для лучшей производительности) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigF/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
</body>
</html>