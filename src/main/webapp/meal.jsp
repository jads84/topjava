<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html lang="ru">
<head>
    <title>Meal</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="meal">
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <form method="post">
        <c:set var="mealTo" value="${requestScope.mealTo}"/>
        <h2>Edit meal</h2>
        <br>
        <div>
            <label>DateTime</label>
            <input type="date" name="mealDate"/>
        </div>
        <div>
            <label>Description</label>
            <input type="text" name="description" value="${mealTo.description}"/>
        </div>
        <div>
            <label>Calories</label>
            <input type="text" name="calories" value="${mealTo.calories}"/>
        </div>
        <div class="button">
            <button type="submit">Save</button>
        </div>
    </form>
</div>
</body>
</html>