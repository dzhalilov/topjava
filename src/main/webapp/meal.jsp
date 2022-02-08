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
<c:set var="act" value="${act}"/>
<h2>${act} meal</h2>
<c:set var="meal" value="${meal}"/>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.id}"/>
    Date Time: <input type="datetime-local" name="dateTime" value="${meal.dateTime}"/><br/>
    Description: <input type="text" name="description" value="${meal.description}"/><br/>
    Calories: <input type="number" name="calories" value="${meal.calories}"/><br/>
    <button type="submit">Save</button>
    <button type="reset" onclick="meals">Cancel</button>
</form>

</body>
</html>