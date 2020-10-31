<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/9/9
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.db.mapper.SubjectMapper" %>
<%@ page import="main.java.domain.Subject" %>
<%@ page import="main.java.db.mapper.UserSubjectMapper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/styles/adminStyles.css" type="text/css">
    <title>Subjects</title>
</head>
<body>
<div align="right">
    <button onclick="logout()">Logout</button>
</div>

<script>
    function logout(){
        alert("Logout successfully");
        window.location.href='/logout.jsp';
    }
</script>
<div align="center">
    <h1>Welcome back, <shiro:principal/>!</h1>
    <h1>All Subjects</h1>
    <table>
        <tr>
            <th>Subject Code</th>
            <th>Subject Name</th>
            <th>Instructors</th>
            <th>Students</th>
        </tr>
        <tr>
            <%
                for (Subject subject : SubjectMapper.getAllSubjects()) {
            %>
            <td><%= subject.getSubjectCode() %></td>
            <td><%= subject.getSubjectName() %></td>
            <td>
                <%= subject.getInstructorNamesAsOneString() %><br>
                <button onclick=addCoordinator("<%=subject.getSubjectCode()%>")>Add coordinator</button>
            </td>
            <td>
                <%= UserSubjectMapper.getAllStudentsWithSubjectAsString(subject.getSubjectCode())%>
            </td>
        </tr>
        <%
            } // for loop
        %>
    </table>
</div>

<div align="center">
    <h2>Add New Subject</h2>
    <form name="AddSubjectForm" action="/subjects" method="post" id="AddSubjectForm">
        <div class="inputLine">Subject Code: <input type = "text" name = "code" id="code"></div>
        <div class="inputLine">Subject Name: <input type = "text" name = "name" id="name"></div>
        <div class="inputLine">Instructor ID: <input type = "text" name = "instructor_id" id="instructor_id"/></div>
        <input class="submitButton" type = "button" value = "Add Subject" onclick="addSubject()"/>
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
    function addSubject() {
        if (document.getElementById("code").value === "") {
            window.alert("Please enter a subject code");
        }
        else if (document.getElementById("name").value === "") {
            window.alert("Please enter a subject name");
        }
        else if (document.getElementById("instructor_id").value === "") {
            window.alert("Please enter an instructor id");
        } else {
            document.getElementById("AddSubjectForm").submit();
        }
    }
</script>
</html>
