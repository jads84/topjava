<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table class="meals">
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    <c:set var="DATE_TIME_FORMATTER" value="${requestScope.DATE_TIME_FORMATTER}"/>
    <c:set var="mealsTo" value="${requestScope.mealsTo}"/>
    <c:forEach items="${mealsTo}" var="mealTo">
        <tr style="color: ${mealTo.excess ? 'red' : 'green'};">
            <td>${mealTo.dateTime.format(DATE_TIME_FORMATTER)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=read&mealId=${mealTo.id}">Update</a></td>
            <td><a href="meals?action=delete&mealId=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>