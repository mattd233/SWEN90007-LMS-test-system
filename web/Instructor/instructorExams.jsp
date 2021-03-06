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
    <link rel="stylesheet" href="/styles/instructorStyles.css" type="text/css">
    <title>View all exams</title>
</head>
<body>
<div align="center">
    <h1>All Exams of Subject <%=request.getParameter("subject_code")%></h1>
    <table>
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
                <button onclick="location.href = '/Instructor/editExam?exam_id=<%=exam.getExamID()%>'">
                    Edit exam
                </button><br>
                <button onclick="if (confirm('Are you sure you want to publish the exam?'))
                    location.href = '/instructor/update_exam_status?action=publish&subject_code=<%=exam.getSubjectCode()%>&exam_id=<%=exam.getExamID()%>' "
                        type="button">
                    Publish exam
                </button><br>
                <button onclick="if (confirm('Are you sure you want to delete the exam?'))
                    location.href = '/instructor/update_exam_status?action=delete&subject_code=<%=exam.getSubjectCode()%>&exam_id=<%=exam.getExamID()%>' "
                        type="button">
                    Delete exam
                </button>
                <%
                    } else if (exam.getStatus() == Exam.ExamStatus.PUBLISHED) {
                %>
                <button onclick="if (confirm('Are you sure you want to close the exam?'))
                    location.href = '/instructor/update_exam_status?action=close&subject_code=<%=exam.getSubjectCode()%>&exam_id=<%=exam.getExamID()%>' "
                        type="button">
                    Close exam
                </button><br>
                <button onclick="if (confirm('Are you sure you want to delete the exam?'))
                        location.href = '/instructor/update_exam_status?action=delete&subject_code=<%=exam.getSubjectCode()%>&exam_id=<%=exam.getExamID()%>' "
                        type="button">
                    Delete exam
                </button>
                <%
                    } else if (exam.getStatus() == Exam.ExamStatus.CLOSED) {
                %>
                <button onclick="location.href='/Instructor/submissions_table?subject_code=<%=exam.getSubjectCode()%>'">
                    Mark exam
                </button>
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
        <input class="submitButton" type="submit" value="Add new exam"/>
    </form>
    <input class="returnButton" type="button" value="Go back to subject page" onclick=window.location.replace("/Instructor/instructorSubjects.jsp?");>
<%--    <a href="/Instructor/instructorSubjects.jsp">back to subject page</a>--%>
</div>

</body>
</html>
