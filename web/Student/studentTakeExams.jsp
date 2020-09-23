<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/23
  Time: 11:04
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

        th {
            text-align: left;
        }
    </style>
    <title>Student take Exams</title>
</head>

<body>
<div align="center">
<%--get parameters directly from the url--%>
<%--    add a link to get back to previous page--%>
<a href="javascript:history.go(-1);">Go back</a>
<%
    String studentID = request.getParameter("studentID");
    String exam_id = request.getParameter("exam_id");
    String title = request.getParameter("title");
    String subject_code = request.getParameter("subject_code");
    String description = request.getParameter("description");
%>
    <table>
        <tr>
            <td>Exam title</td>
            <td><%=title%></td>
        </tr>
        <tr>
            <td>Exam Description</td>
            <td><%=description%></td>
        </tr>
    </table>

<%--give a notive board and add a button to take the exam--%>
Notice that the exam only allow single attempt!<br/>
<a href="studentAnswerQuestions.jsp?student_id=<%=studentID%>&exam_id=<%=exam_id%>&subject_code=<%=subject_code%>&title=<%=title%>">
    I understand, start the exam now.</a>
</div>
</body>
</html>
