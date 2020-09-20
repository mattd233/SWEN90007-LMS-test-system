<%--<%@ page import="db.mapper.InstructorMapper" %>&lt;%&ndash;--%>
<%--  Created by IntelliJ IDEA.--%>
<%--  User: Matt--%>
<%--  Date: 2020/9/19--%>
<%--  Time: 20:45--%>
<%--  To change this template use File | Settings | File Templates.--%>
<%--&ndash;%&gt;--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <style>--%>
<%--        table, th, td {--%>
<%--            border: 1px solid black;--%>
<%--            border-collapse: collapse;--%>
<%--        }--%>
<%--        th, td {--%>
<%--            padding: 5px;--%>
<%--        }--%>
<%--        th {--%>
<%--            text-align: left;--%>
<%--        }--%>
<%--    </style>--%>
<%--    <title>View all subjects</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--<div align="center">--%>
<%--    <table style="width:70%">--%>
<%--        <tr>--%>
<%--            <th>Subject Code</th>--%>
<%--            <th>Subject Name</th>--%>
<%--            <th></th>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <%  int userID = 0;--%>
<%--                if (session.getAttribute("user_id") == null) {--%>
<%--                    response.sendRedirect("/login.jsp");--%>
<%--                } else {--%>
<%--                    userID = (int) session.getAttribute("user_id");--%>
<%--                    System.out.println(userID);--%>
<%--                }--%>
<%--                for (Subject subject : Objects.requireNonNull(InstructorMapper.getAllSubjectsWithInstructor(userID))) {--%>
<%--            %>--%>
<%--            <td><%=subject.getSubjectCode()%></td>--%>
<%--            <td><%=subject.getSubjectName()%></td>--%>
<%--            <td><a href="instructorExams.jsp">View exams</a></td>--%>
<%--        </tr>--%>
<%--        <%--%>
<%--            } // for loop--%>
<%--        %>--%>
<%--    </table>--%>
<%--</div>--%>
<%--</body>--%>
<%--</html>--%>
