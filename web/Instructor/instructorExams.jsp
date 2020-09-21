<%@ page import="db.mapper.InstructorMapper" %>
<%@ page import="domain.Exam" %>
<%@ page import="db.mapper.ExamMapper" %>
<%@ page import="java.util.Objects" %><%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/9/19
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
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
    </style>
    <title>View all exams</title>
    <link rel="stylesheet" href="Menu.css">
</head>
<body>
<div align="center">
    <table style="width:70%">
        <tr>
            <th>Title</th>
            <th>Description</th>
            <th>Status</th>
            <th></th>
        </tr>
        <tr>
            <%
                String subjectCode = request.getParameter("subject_code");
                for (Exam exam : Objects.requireNonNull(ExamMapper.getAllExamsWithSubjectCode(subjectCode))) {
            %>
            <td><%=exam.getTitle()%></td>
            <td><%=exam.getDescription()%></td>
            <td><%=exam.getStatus()%></td>
            <td>
                <div class=menu>
                    <ul>
                        <li><a href="#">Actions</a>
                            <ul>
                                <li><a href="#">Publish</a></li>
                                <li><a href="#">Close</a></li>
                                <li><a href="#">Delete</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </td>
        </tr>
        <%
            } // for loop
        %>
    </table>
    <form name="add_new_exam" action="createNewExam.jsp?subject_code=<%=request.getParameter("subject_code")%>" method="post">
        <input type="submit" value="Add new exam"/>
    </form>
</div>

</body>
</html>
