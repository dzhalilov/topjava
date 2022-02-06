<%@ page import="ru.javawebinar.topjava.repository.MealRepo" %>
<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: User
  Date: 06.02.2022
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="#">Add Meal</a></h3>
<hr>
<h2>Meals</h2>
<table class="table-cell" border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
<%--        <th>Update</th>--%>
<%--        <th>Delete</th>--%>
    </tr>
    <%
        List<MealTo> list = (List<MealTo>) request.getAttribute("mealTo");
        for (MealTo meal : list){
            %>
                <tr style="color: <%=meal.isExcess() ? "red" : "green"%>">
                    <td> <%=meal.getDateTime().toString().replace("T", " ")%></td>
                    <td> <%=meal.getDescription()%></td>
                    <td> <%=meal.getCalories()%></td>
<%--                    <td> <a href="" </td>--%>
                </tr>
            <%
        }
    %>
</table>
</body>
</html>