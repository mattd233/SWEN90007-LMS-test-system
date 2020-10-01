<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/22
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.db.mapper.SubmissionMapper" %>
<%@ page import="main.java.domain.Exam" %>
<%@ page import="main.java.domain.Submission" %>
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
    <title>Student view exams</title>
</head>
<body align = "center">

<%--header--%>
<h1>Detailed view of the exam</h1>

<div align="center">

<%--    add a link to get back to previous page--%>
<a href="studentHomePage.jsp">Go back to Home Page.</a>

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
            <td class="exam_id"><%=exam_id%></td>
        </tr>
        <tr>
            <td>Exam title</td>
            <td class="exam_title"><%=title%></td>
        </tr>
        <tr>
            <td>Subject code</td>
            <td class="subject_code"><%=subject_code%></td>
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
            <td>Submission</td>
            <td>
                <%
                    int student_id = Integer.parseInt(studentID);
                    // check whether it is on the submissions using exam_id combined with student_id
                    if (SubmissionMapper.checkSubmission(exam_id, student_id)){
                %>
                Submitted
                <%
                    } // end if
                    else {
                %>
                Not Submitted
                <%
                    }
                %>
            </td>
        </tr>
    </table>
    <%
        if (status.equals("PUBLISHED") && !SubmissionMapper.checkSubmission(exam_id, student_id)){
    %>
        <a href = "studentTakeExams.jsp?studentID=<%=studentID%>&exam_id=<%=exam_id%>"><br> Take the exam now.</a>
    <%
        } else if (status.equals("PUBLISHED") && SubmissionMapper.checkSubmission(exam_id,student_id)){
            Submission submission = SubmissionMapper.getSubmissionByIDs(exam_id, student_id);
            assert submission != null;
            boolean isMarked = submission.isMarked();
            if (isMarked){
    %>
        <a href = "studentTakeExams.jsp?studentID=<%=studentID%>&exam_id=<%=exam_id%>"><br> View result.</a>
    <%
            } // end if
            else {
    %>
            <br>The exam is not graded yet.
    <%
            } // end else
        } // end else if
    %>
</div>
</body>
</html>
