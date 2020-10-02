<%--
  Created by IntelliJ IDEA.
  User: jiayuli
  Date: 22/9/20
  Time: 6:45 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.db.mapper.SubmissionMapper" %>
<%@ page import="main.java.db.mapper.QuestionMapper" %>
<%@ page import="main.java.db.mapper.SubmittedQuestionMapper" %>
<%@ page import="main.java.domain.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/Instructor/MarkingViews/markingStyles.css" type="text/css">
    <title>Student's Submission</title>
</head>
<body>
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
    <!-- Show subject code and exam title -->
    <h1><%=exam.getSubjectCode()%> <%=exam.getTitle()%></h1>

    <!-- Show message if there's no submission from this student to this exam -->
    <%
        // Get the submission
        Submission submission = SubmissionMapper.getSubmissionByIDs(examID, userID);
        if (submission == null) {
    %>
    <p>This student has no submission for this exam.</p>

    <!-- Show submission information -->
    <%
        } else {
            String submissionTime = submission.getSubmissionTime().toString();
    %>
    <p>Submission time: <%=submissionTime%></p>
    <%
        // Display questions and marks
        List<Question> questions = QuestionMapper.getAllQuestionsWithExamID(examID);
        for (int i=0; i<questions.size(); i++) {
            Question question = questions.get(i);
            SubmittedQuestion answer = SubmittedQuestionMapper.getSubmittedQuestion(examID, userID, question.getQuestionID());
            String displayMarks = "";
            if (answer.getChoiceNumber() == 0 && answer.getShortAnswer() == null) {
                displayMarks = "0";
            } else {
                if (answer.isMarked()) {
                    displayMarks = Float.valueOf(answer.getMarks()).toString();
                }
            }
    %>
    <form method="post">
        <h3><%=question.getTitle()%>: <input name="marksQ<%=question.getQuestionID()%>" size="5" value="<%=displayMarks%>"> out of <%=question.getMarks()%></h3>
        <p>Question: <%=question.getDescription()%></p>
        <%
                    if (answer.getChoiceNumber() == 0 && answer.getShortAnswer() == null) {
        %>
        <p>Student did not answer this question.</p>
        <%
                    } else {
        %>
        <p>Student's Answer: </p>
        <%
                        // Display student's answer
                        if (question instanceof MultipleChoiceQuestion) {
                            List<Choice> choices = ((MultipleChoiceQuestion) question).getChoices();
                            for (Choice choice : choices) {
                                String selectionPrepend = "";
                                if (choice.getChoiceID() == answer.getChoiceNumber()) {
                                    selectionPrepend = "->";
                                }
        %>
        <p><%=selectionPrepend%> <%=choice.getChoiceID()%>. <%=choice.getChoiceDescription()%></p>
        <%
                            }
                        } else if (question instanceof ShortAnswerQuestion) {
                                String shortAnswer = answer.getShortAnswer();
        %>
        <p><%=shortAnswer%></p>
        <%
                        }
        %>
        <p>Fudge points: <input name="fudgePoints" size="5" value=<%=submission.getFudgePoints()%>></p>
        <input type="submit" value="Update marks">
        <%
                    }
                }
            }
        %>
    </form>
</body>
</html>
