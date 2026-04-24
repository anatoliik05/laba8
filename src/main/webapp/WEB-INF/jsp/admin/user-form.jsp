<%--@elvariable id="user" type="com.example.laba8.entity.UserEntity"--%>
<%--@elvariable id="isNewUser" type="java.lang.Boolean"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${isNewUser ? 'Добавить нового пользователя' : 'Редактировать пользователя'}"/></title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 20px; }
        .container { max-width: 600px; }
    </style>
</head>
<body>
<div class="container">
<h2 class="mb-4"><c:out value="${isNewUser ? 'Добавить нового пользователя' : 'Редактировать пользователя'}"/></H2>

    <form:form action="${isNewUser ? '/admin/users' : '/admin/users/'.concat(user.id)}" method="post" modelAttribute="user">
    <sec:csrfInput /> <%-- Обязательно для POST запросов с Spring Security --%>
    <div class="mb-3">
        <label for="name" class="form-label">Имя пользователя:</label>
        <form:input path="name" id="name" class="form-control" required="true"/>
            <%-- Здесь можно добавить <form:errors path="name" cssClass="text-danger"/> для валидации --%>
    </div>
    <div class="mb-3">
        <label for="email" class="form-label">Email:</label>
        <form:input type="email" path="email" id="email" class="form-control" required="true"/>
            <%-- <form:errors path="email" cssClass="text-danger"/> --%>
    </div>
    <div class="mb-3">
        <label for="password" class="form-label">Пароль:
            <c:if test="${!isNewUser}">(Оставьте пустым, чтобы не менять)</c:if>
        </label>
        <form:password path="password"
                       id="password"
                       class="form-control"
                       required="${isNewUser ? 'required' : ''}" />
            <%-- <form:errors path="password" cssClass="text-danger"/> --%>
    </div>

    <%-- Поле роли (для редактирования, чтобы админ мог менять) --%>
    <div class="mb-3">
        <label for="role" class="form-label">Роль:</label>
        <form:select path="role" id="role" class="form-select">
            <form:option value="ROLE_USER" label="Пользователь"/>
            <form:option value="ROLE_ADMIN" label="Администратор"/>
        </form:select>
    </div>

    <%-- Флаг deleted (только для редактирования) --%>
    <c:if test="${!isNewUser}">
        <div class="form-check mb-3">
            <form:checkbox path="deleted" id="deleted" class="form-check-input"/>
            <label for="deleted" class="form-check-label">Пометить как удаленного</label>
        </div>
    </c:if>

    <button type="submit" class="btn btn-success">
        <c:out value="${isNewUser ? 'Создать' : 'Сохранить изменения'}"/>
    </button>
    <a href="/admin/users" class="btn btn-secondary ms-2">Отмена</a>
</form:form>
</div>

<!-- Bootstrap JS (bundle includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>