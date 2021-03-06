<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/23
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.domain.Exam" %>
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
<%--    add a link to get back to previous page--%>
<a href="javascript:history.go(-1);">Go back</a>
    <%--get parameters directly from the url--%>
<%
    String studentID = request.getParameter("studentID");
    String examID = request.getParameter("exam_id");;
    int exam_id = Integer.parseInt(examID);
    Exam exam = ExamMapper.getExamByID(exam_id);
    assert exam != null;
    String title = exam.getTitle();
    String subject_code = exam.getSubjectCode();
    String description = exam.getDescription();
%>
    <table>
        <tr>
            <td>Subject code</td>
            <td><%=subject_code%></td>
        </tr>
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
<form name=start method="post" target="_blank"
      action="studentAnswerQuestions.jsp?studentID=<%=studentID%>&exam_id=<%=exam_id%>&question_index=0">
    <input type="button" value="I understand, start the exam now." onclick="javascript:{this.disabled=true;document.start.submit();}">
</form>

</div>
</body>

</html>
