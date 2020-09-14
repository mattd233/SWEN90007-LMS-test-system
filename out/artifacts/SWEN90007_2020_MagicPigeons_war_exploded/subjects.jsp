<%@ page import="domain.Subject" %><%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/9/9
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subjects</title>
</head>
<body>
<div align="center">
    <table style="width:70%">
        <tr>
            <th>Subject Code</th>
            <th>Subject Name</th>
            <th>Coordinator</th>
        </tr>
        <tr>
            <%
                for (Subject subject : Subject.getAllSubjects()) {
            %>
            <td><%= subject.getSubjectCode() %></td>
            <td><%= subject.getName() %></td>
            <td><%= subject.getCoordinator() %></td>
        </tr>
        <%
            } // for loop
        %>
    </table>
</div>
<div align="center">
    <form name="AddSubjectForm" action="/subjects" method="post">
        Subject Code: <input type = "text" name = "code">
        <br />
        Subject Name: <input type = "text" name = "name">
        <br />
        Coordinator: <input type = "text" name = "coordinator_name" />
        <br />
        <input type = "submit" value = "Add New Subject" />
    </form>
</div>
</body>
</html>
