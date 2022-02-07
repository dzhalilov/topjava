<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<c:set var="param" value="meal"/>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${item.id}"/>
<%--    Date Time: <input type="datetime-local" name="dateTime" value="<c:out value="${item.dateTime}" />"/><br/>--%>
    Description: <input type="text" name="description" value="${param.description}"/><br/>
    Calories: <input type="text" name="description" value="${param.calories}"/><br/>
    <button type="submit">Save</button>
    <button onclick="meals">Cancel</button>
</form>

</body>
</html>