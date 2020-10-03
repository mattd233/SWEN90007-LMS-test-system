<%--
  Created by IntelliJ IDEA.
  User: jiayuli
  Date: 23/9/20
  Time: 11:54 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.db.mapper.SubmissionMapper" %>
<%@ page import="main.java.db.mapper.QuestionMapper" %>
<%@ page import="main.java.db.mapper.SubmittedQuestionMapper" %>
<%@ page import="main.java.domain.Exam" %>
<%@ page import="main.java.domain.Submission" %>
<%@ page import="main.java.domain.Question" %>
<%@ page import="main.java.domain.SubmittedQuestion" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/styles/instructorMarkingStyles.css" type="text/css">
    <title>Update success!</title>
</head>
<body>
<div align="center">
    <%
        String examIdStr = request.getParameter("examID");
        String userIdStr = request.getParameter("userID");
        int examID = 0;
        int userID = 0;
        try {
            examID = Integer.valueOf(examIdStr);
            userID = Integer.valueOf(userIdStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Exam exam = ExamMapper.getExamByID(examID);
    %>
    <h1><%=exam.getSubjectCode()%> <%=exam.getTitle()%></h1>
    <h3 class="successMsg">Your markings are successfully saved to the database.</h3>
    <table style="width:30%">
        <tr>
            <th>Question Number</th>
            <th>Marks</th>
        </tr>
        <%
            Submission submission = SubmissionMapper.getSubmissionByIDs(examID, userID);
            List<Question> questions = exam.getQuestions();
            for (int i=0; i<questions.size(); i++) {
                Question question = questions.get(i);
                SubmittedQuestion answer = SubmittedQuestionMapper.getSubmittedQuestion(examID, userID, question.getQuestionNumber());
                String displayMarks = "Not marked";
                if (answer.isMarked()) {
                    displayMarks = Float.valueOf(answer.getMarks()).toString();
                    displayMarks += " out of ";
                    displayMarks += Float.valueOf(question.getMarks()).toString();
                }
        %>
            <tr>
                <td><%=question.getQuestionNumber()%></td>
                <td><%=displayMarks%></td>
            </tr>
        <%
            }
        %>
    </table>
    <p>Fudge points: <%=submission.getFudgePoints()%></p>
    <a href="/submissions_table?subject_code=<%=exam.getSubjectCode()%>">Back to table view</a>
</div>
</body>
</html>
