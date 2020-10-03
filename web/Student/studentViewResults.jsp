<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/10/1
  Time: 23:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.db.mapper.SubmissionMapper" %>
<%@ page import="main.java.domain.Exam" %>
<%@ page import="main.java.domain.Question" %>
<%@ page import="main.java.domain.Submission" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>student view results</title>
</head>
<body align = "center">
<%--get parameters directly from the url--%>
<%
    String studentID = request.getParameter("studentID");
    int student_id = Integer.parseInt(studentID);
    String examID = request.getParameter("exam_id");
    int exam_id = Integer.parseInt(examID);
    Exam exam = ExamMapper.getExamByID(exam_id);
    assert exam != null;
    String title = exam.getTitle();
    String subject_code = exam.getSubjectCode();

%>
<%--header--%>
<h1>View Result of <%=title%></h1>

<%--    add a link to get back to home page--%>
<a href="studentHomePage.jsp">Go back to Home Page.</a><br>

<%--    fill the table--%>
<table border="1" align = "center">
    <tr>
        <td>Exam title</td>
        <td class="exam_title"><%=title%></td>
    </tr>
    <tr>
        <td>Subject code</td>
        <td class="subject_code"><%=subject_code%></td>
    </tr>
    <tr>
        <td>Marks</td>
        <%
            Submission submission = SubmissionMapper.getSubmissionByIDs(exam_id, student_id);
            boolean isMarked = submission.isMarked();
            float marks = submission.getMarks();
            //TODO display xx out of xx points
            List<Question> questionList = exam.getQuestions();
            // count the total marks of this exam
            float total = 0;
            for (Question question : questionList) {
                System.out.println(question.getDescription());
                float single_marks = question.getMarks();
                total += single_marks;
            }
            %>

        <td>
            <%=marks%> out of <%=total%><br>
            <%
                if (!isMarked){%>
            <br>Some of the questions have not been graded yet.<br>
            <%
                }
            %></td>
    </tr>
</table>

</body>
</html>
