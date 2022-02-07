<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h3><a href="#">Add Meal</a></h3>
<h2>Meals</h2>
<table class="table-cell" border="1">
    <tr style="background-color: darkgrey">
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${mealsTo}" var="meal">
        <tr style="color: ${meal.excess ? "red" : "green"} ">
            <td>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/>
            </td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td>
                <form method="post">
                    <a href="meals?id=<c:out value="${meal.id}"/>">Update</a>
                </form>
            </td>
            <td>
                <form method="get">
                    <a href="meals?id=<c:out value="${meal.id}"/>">Delete</a>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>