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
    <title>Login Page</title>
  </head>
  <body>
  <form name="LoginForm" action="/login" method="post">
    username: <input type = "text" name = "userName">
    <br />
    password: <input type = "text" name = "passWord">
    <br />
    <input type = "submit" value = "Login" />
  </form>
  </body>
</html>
