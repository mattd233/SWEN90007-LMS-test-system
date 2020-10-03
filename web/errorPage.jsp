<%--
  Created by IntelliJ IDEA.
  User: jiayuli
  Date: 23/9/20
  Time: 2:54 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <%
        Object errMsgAttr = request.getAttribute("errMsg");
        String errMsg = "Something wrong has happened. Please try again later.";
        if (errMsgAttr != null && !errMsgAttr.toString().equals("")) {
            errMsg = errMsgAttr.toString();
        }
    %>
    <p>Error: <%=errMsg%></p>
</body>
</html>
