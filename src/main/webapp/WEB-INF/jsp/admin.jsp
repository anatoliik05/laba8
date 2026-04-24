<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Управление Пользователями</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 20px; }
        .container { max-width: 1200px; }
        .table-actions button, .table-actions a { margin-right: 5px; margin-bottom: 5px; }
        .role-admin { color: #dc3545; font-weight: bold; } /* Красный для админа */
        .role-user { color: #007bff; } /* Синий для пользователя */
        .status-deleted { color: #6c757d; font-style: italic; } /* Серый для удаленных */
        .status-active { color: #28a745; font-weight: bold; } /* Зеленый для активных */
    </style>
</head>
<body>
<div class="container">
    <h2 class="mb-4">Управление Пользователями</h2>

    <c:if test="${not empty successMessage}">
    <div class="alert alert-success mt-3" role="alert">
            ${successMessage}
    </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger mt-3" role="alert">
            ${errorMessage}
    </div>
    </c:if>

    <div class="mb-3">
        <a href="/admin/users/new" class="btn btn-primary">Добавить нового пользователя</a>
        <a href="/welcome" class="btn btn-secondary ms-2">Вернуться на главную</a>
    </div>

    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th>ID</th>
            <th>Имя пользователя</th>
            <th>Email</th>
            <th>Роль</th>
            <th>Статус</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>
                <c:choose>
                    <c:when test="${user.role == 'ROLE_ADMIN'}">
                        <span class="role-admin">${user.role}</span>
                    </c:when>
                    <c:otherwise>
                        <span class="role-user">${user.role}</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${user.deleted}">
                        <span class="status-deleted">Удален</span>
                    </c:when>
                    <c:otherwise>
                        <span class="status-active">Активен</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="table-actions">
                <a href="/admin/users/${user.id}/edit" class="btn btn-sm btn-info">Редактировать</a>
                <c:if test="${user.name != pageContext.request.userPrincipal.name}">
                    <c:if test="${user.role != 'ROLE_ADMIN'}">
                        <form action="/admin/users/${user.id}/make-admin" method="post" style="display:inline;">
                            <sec:csrfInput />
                            <button type="submit" class="btn btn-sm btn-warning">Сделать админом</button>
                        </form>
                    </c:if>
                    <c:if test="${user.role == 'ROLE_ADMIN'}">
                        <form action="/admin/users/${user.id}/make-user" method="post" style="display:inline;">
                            <sec:csrfInput />
                            <button type="submit" class="btn btn-sm btn-outline-warning">Сделать пользователем</button>
                        </form>
                    </c:if>
                </c:if>

                <c:if test="${!user.deleted}">
                    <form action="/admin/users/${user.id}/delete" method="post" style="display:inline;"
                          onsubmit="return confirm('Вы уверены, что хотите удалить пользователя ${user.name}?');">
                        <sec:csrfInput />
                        <button type="submit" class="btn btn-sm btn-danger">Удалить</button>
                    </form>
                </c:if>
                <c:if test="${user.deleted}">
                    <form action="/admin/users/${user.id}/restore" method="post" style="display:inline;"
                          onsubmit="return confirm('Вы уверены, что хотите восстановить пользователя ${user.name}?');">
                        <sec:csrfInput />
                        <button type="submit" class="btn btn-sm btn-success">Восстановить</button>
                    </form>
                </c:if>
            </td>
        </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Bootstrap JS (bundle includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
