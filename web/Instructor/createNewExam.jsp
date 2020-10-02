<%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/9/21
  Time: 22:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create an exam</title>
</head>
<body>
<div>
    <body>
    <form name="CreateExam" action="/Instructor/createExam" method="post">
        Title: <input type="text" name="title">
        <br />
        Description:
        <input type="text" name="exam_description">
<%--        <br />--%>
<%--        <textarea name="exam_description " rows="5" cols="30"> </textarea>--%>
        <br />
        <input type="hidden" name="code" value="<%=request.getParameter("subject_code")%>">
        <input type="submit" value="Create exam" />
    </form>
</div>
</body>
</html>
