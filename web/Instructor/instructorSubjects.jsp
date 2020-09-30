<%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/9/19
  Time: 20:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Objects" %>
<%@ page import="main.java.db.mapper.SubjectMapper" %>
<%@ page import="main.java.domain.Subject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }

        th {
            text-align: left;
        }
    </style>
    <title>View all subjects</title>
</head>
<body>
<div align="center">
    <table style="width:70%">
        <tr>
            <th>Subject Code</th>
            <th>Subject Name</th>
            <th></th>
        </tr>
        <tr>
            <% int userID = 0;
                if (session.getAttribute("user_id") == null) {
                    response.sendRedirect("/login.jsp");
                } else {
                    userID = (int) session.getAttribute("user_id");
                }
                for (Subject subject : Objects.requireNonNull(SubjectMapper.getAllSubjectsWithInstructor(userID))) {
            %>
            <td><%=subject.getSubjectCode()%>
            </td>
            <td><%=subject.getSubjectName()%>
            </td>
            <td>
                <form action="instructorExams.jsp">
                    <input type="submit" value="View Exams">
                    <input type="hidden" name="subject_code" value="<%=subject.getSubjectCode()%>"/>
                </form>
            </td>
        </tr>
        <%
            } // for loop
        %>
    </table>
</div>
</body>
</html>
