<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/10/31
  Time: 20:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logout</title>
</head>
<body>
<script type="text/javascript" language="javascript">
    alert("Logout successfully");
</script>
<%
    session.invalidate();
    response.sendRedirect("login.jsp");
%>
</body>
</html>
