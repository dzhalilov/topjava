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
    <table>
        <tr>
            <input type="hidden" name="id" value="${meal.id}"/>
            <td>
                Date Time:<br/>
                Description:<br/>
                Calories:<br/>
            </td>
            <td>
                <input type="datetime-local" name="dateTime" value="${meal.dateTime}"/><br/>
                <input type="text" name="description" value="${meal.description}"/><br/>
                <input type="number" name="calories" value="${meal.calories}"/><br/>
            </td>
        </tr>
        <tr>
            <td>
                <input type="submit" name="submit" value="Save">
            </td>
            <td>
                <a href="meals">
                    <button type="button">Cancel</button>
                </a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>