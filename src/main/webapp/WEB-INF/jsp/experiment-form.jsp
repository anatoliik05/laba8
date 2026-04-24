<%--@elvariable id="experiment" type="com.example.laba8.entity.Experiment"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${experiment.id == null ? 'Добавить новый эксперимент' : 'Редактировать эксперимент'}"/></title>
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
            <c:out value="${experiment.id == null ? 'Добавить новый эксперимент' : 'Редактировать эксперимент'}"/>
        </h2>

        <form:form action="${pageContext.request.contextPath}/experiments/save" method="post" modelAttribute="experiment">
            <form:hidden path="id"/>
            <%-- Если есть поле deleted, которое нужно сохранять, можно добавить: --%>
            <%-- <form:hidden path="deleted"/> --%>

            <div class="mb-3">
                <label for="team" class="form-label">Бригада:</label>
                <form:select path="team.id" id="team" class="form-select" required="true">
                    <form:option value="" label="-- Выберите бригаду --"/>
                    <form:options items="${teams}" itemValue="id" itemLabel="teamName"/>
                </form:select>
                <form:errors path="team.id" cssClass="text-danger"/>
            </div>

            <div class="mb-3">
                <label for="probe" class="form-label">Проба:</label>
                <form:select path="probe.id" id="probe" class="form-select" required="true">
                    <form:option value="" label="-- Выберите пробу --"/>
                    <form:options items="${probes}" itemValue="id" itemLabel="substanceName"/>
                </form:select>
                <form:errors path="probe.id" cssClass="text-danger"/>
            </div>

            <div class="mb-3">
                <label for="result" class="form-label">Результат:</label>
                <form:textarea path="result" id="result" class="form-control" rows="3" required="true"/>
                <form:errors path="result" cssClass="text-danger"/>
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
