<%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 13/09/2020
  Time: 9:17 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/styles/login.css" type="text/css">
    <title>Login Page</title>
</head>
<body>
    <div class="center">
        <form name="LoginForm" action="/login" method="post">
            <div class="inputLine">username: <input type="text" name="userName"></div><br>
            <div class="inputLine">password: <input type="text" name="passWord"></div><br>
            <input class="submitButton" type="submit" value="Login"/>
        </form>
    </div>
</body>
</html>
