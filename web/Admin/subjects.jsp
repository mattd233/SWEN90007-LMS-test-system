<%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/9/9
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.db.mapper.SubjectMapper" %>
<%@ page import="main.java.domain.Subject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subjects</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 5px;
        }
        th {
            text-align: left;
        }
        form {
            margin: 5px;
        }
    </style>
</head>
<body>
<div align="center">
    <table style="width:70%">
        <tr>
            <th>Subject Code</th>
            <th>Subject Name</th>
            <th>Coordinator</th>
            <th></th>
        </tr>
        <tr>
            <%
                for (Subject subject : SubjectMapper.getAllSubjects()) {
            %>
            <td><%= subject.getSubjectCode() %></td>
            <td><%= subject.getSubjectName() %></td>
            <td><%= subject.getInstructorNamesAsOneString() %></td>
            <td><button onclick=addCoordinator("<%=subject.getSubjectCode()%>")>Add coordinator</button></td>
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
        Instructor ID: <input type = "text" name = "instructor_id" />
        <br />
        <input type = "submit" value = "Add New Subject" />
    </form>
</div>
</body>
<script>
    function addCoordinator(subjectCode) {
        var instructorID = prompt("Please enter instructor ID");
        if (instructorID != null && instructorID !== "") {
            var target = "/subjects?instructor_id=".concat(instructorID, "&subject_code=", subjectCode);
            window.location.replace(target);
        }
    }
</script>
</html>
