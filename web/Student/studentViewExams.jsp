<%@ page import="domain.Exam" %>
<%@ page import="db.mapper.ExamMapper" %>
<%@ page import="db.mapper.SubmissionMapper" %><%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/22
  Time: 22:40
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
    <title>Student view exams</title>
</head>
<body align = "center">

<%--header--%>
<h1>Detailed view of the exam</h1>

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
    String status = exam.getStatus().name();
%>

    <%--    fill the table--%>
    <table border="1" align = "center">
        <tr>
            <td>Exam id</td>
            <td><%=exam_id%></td>
        </tr>
        <tr>
            <td>Exam title</td>
            <td><%=title%></td>
        </tr>
        <tr>
            <td>Subject code</td>
            <td><%=subject_code%></td>
        </tr>
        <tr>
            <td>Exam description</td>
            <td><%=description%></td>
        </tr>
<%--    if the exam is published, the student can take the exam--%>
        <tr>

            <td>Exam status</td>
            <td>
                <%
                    if (status.equals("CLOSED") || status.equals("PUBLISHED")){
                %>
                    <%=status%>
                <%
                    } // end if
                %>

            </td>
        </tr>
        <tr>
            <td>Attendance</td>
            <td>
                <%
                    int student_id = Integer.parseInt(studentID);
                    // check whether it is on the submissions using exam_id combined with student_id
                    if (SubmissionMapper.checkSubmission(exam_id, student_id)){
                %>
                Attended
                <%
                    } // end if
                    else {
                %>
                Not Attended
                <%
                    }
                %>
            </td>
        </tr>
    </table>
    <%
        if (status.equals("PUBLISHED") && !SubmissionMapper.checkSubmission(exam_id, student_id)){
    %>
        <a href = "studentTakeExams.jsp?studentID=<%=studentID%>&exam_id=<%=exam_id%>"> Take the exam now.</a>
    <%
        }
    %>
</div>
</body>
</html>
