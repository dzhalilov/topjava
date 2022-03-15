<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title><spring:message code="meal.meal"/></title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body>
<section>
    <h3><a href="/topjava/"/><spring:message code="app.home"/></h3>
    <hr>
    <h2>
        <c:choose>
            <c:when test="${param.action == 'create'}">
                <spring:message code="addMeal.create"/>
            </c:when>
            <c:otherwise>
                <spring:message code="addMeal.edit"/>
            </c:otherwise>
        </c:choose>
    </h2>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="addMeal.dateTime"/></dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="addMeal.description"/></dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="addMeal.calories"/></dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><spring:message code="addMeal.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="addMeal.cancel"/></button>
    </form>
</section>
</body>
</html>
