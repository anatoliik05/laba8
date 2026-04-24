<%--@elvariable id="probe" type="com.example.laba8.entity.Probe"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${probe.id == null ? 'Добавить новую пробу' : 'Редактировать пробу'}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .form-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>
<div class="container">
    <div class="form-container">
        <h2 class="mb-4 text-center">
            <c:out value="${probe.id == null ? 'Добавить новую пробу' : 'Редактировать пробу'}"/>
        </h2>

        <form:form action="${pageContext.request.contextPath}/probes/save" method="post" modelAttribute="probe">
            <form:hidden path="id"/>
            <%-- Если есть поле deleted, которое нужно сохранять, можно добавить: --%>
            <%-- <form:hidden path="deleted"/> --%>

            <div class="mb-3">
                <label for="substanceName" class="form-label">Название вещества:</label>
                <form:input type="text" path="substanceName" id="substanceName" class="form-control" required="true"/>
                <form:errors path="substanceName" cssClass="text-danger"/>
            </div>

            <div class="mb-3">
                <label for="weight" class="form-label">Вес (г):</label>
                <form:input type="number" path="weight" id="weight" class="form-control" step="0.01" required="true"/>
                <form:errors path="weight" cssClass="text-danger"/>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                <button type="submit" class="btn btn-primary me-md-2">Сохранить</button>
                <a href="${pageContext.request.contextPath}/welcome" class="btn btn-secondary">Отмена</a>
            </div>
        </form:form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigF/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
</body>
</html>
