<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<c:set var="meal" value="${requestScope.meal}"/>
<html lang="ru">
<head>
    <title>Meal</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="meal">
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <form method="post" action="meals">
        <h2>${meal==null ? 'Create' : 'Edit'} meal</h2>
        <br>
        <div>
            <label>DateTime</label>
            <input type="datetime-local" name="dateTime" value="${meal.dateTime}"/>
        </div>
        <div>
            <label>Description</label>
            <input type="text" name="description" value="${meal.description}"/>
        </div>
        <div>
            <label>Calories</label>
            <input type="text" name="calories" value="${meal.calories}"/>
        </div>
        <div class="button">
            <input type="hidden" name="action" value="${meal==null ? 'create' : 'update'}">
            <input type="hidden" name="id" value="${meal.id}">
            <button type="submit">Save</button>
        </div>
    </form>
</div>
</body>
</html>