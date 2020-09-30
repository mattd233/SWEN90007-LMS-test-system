<%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/9/19
  Time: 20:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Objects" %>
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
                <%
                    if (exam.getStatus() == Exam.ExamStatus.UNPUBLISHED) {
                %>
                <a href="/instructor/update_exam_status?action=publish&subject_code=<%=exam.getSubjectCode()%>&exam_id=<%=exam.getExamID()%>">
                    Publish exam
                </a><br>
                <a href="/instructor/update_exam_status?action=delete&subject_code=<%=exam.getSubjectCode()%>&exam_id=<%=exam.getExamID()%>">
                    Delete exam
                </a>
                <%
                    } else if (exam.getStatus() == Exam.ExamStatus.PUBLISHED) {
                %>
                <a href="/instructor/update_exam_status?action=close&subject_code=<%=exam.getSubjectCode()%>&exam_id=<%=exam.getExamID()%>">
                    Close exam
                </a><br>
                <a href="/instructor/update_exam_status?action=delete&subject_code=<%=exam.getSubjectCode()%>&exam_id=<%=exam.getExamID()%>">
                    Delete exam
                </a>
                <%
                    } else if (exam.getStatus() == Exam.ExamStatus.CLOSED) {
                %>
                <a href="/submissions_table?subject_code=<%=exam.getSubjectCode()%>">Mark exam</a>
                <%
                    }
                %>
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
