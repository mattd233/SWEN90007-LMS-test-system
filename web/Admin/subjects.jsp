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
    <link rel="stylesheet" href="/styles/adminStyles.css" type="text/css">
    <title>Subjects</title>
</head>
<body>
<div align="center">
    <h1>All Subjects</h1>
    <table>
        <tr>
            <th>Subject Code</th>
            <th>Subject Name</th>
            <th>Instructors</th>
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
    <h2>Add New Subject</h2>
    <form name="AddSubjectForm" action="/subjects" method="post">
        <div class="inputLine">Subject Code: <input type = "text" name = "code"></div>
        <div class="inputLine">Subject Name: <input type = "text" name = "name"></div>
        <div class="inputLine">Instructor ID: <input type = "text" name = "instructor_id" /></div>
        <input class="submitButton" type = "submit" value = "Add Subject" />
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
