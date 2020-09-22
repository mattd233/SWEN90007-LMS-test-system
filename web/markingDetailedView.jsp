<%--
  Created by IntelliJ IDEA.
  User: jiayuli
  Date: 22/9/20
  Time: 6:45 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="db.mapper.ExamMapper" %>
<%@ page import="db.mapper.SubmissionMapper" %>
<%@ page import="java.util.List" %>
<%@ page import="db.mapper.QuestionMapper" %>
<%@ page import="domain.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Submission</title>
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
    <h1><%=exam.getSubjectCode()%> <%=exam.getTitle()%></h1>
    <%
        Submission submission = SubmissionMapper.getSubmissionByIDs(examID, userID);
        List<Question> questions = QuestionMapper.getAllQuestionsWithExamID(examID);
        for (int i=0; i<questions.size(); i++) {
            Question question = questions.get(i);
            SubmittedQuestion answer = SubmissionMapper.getSubmittedQuestion(examID, userID, question.getQuestionNumber());
    %>
        <h3><%=question.getTitle()%>: <input name="marks" size="2">/<%=question.getMarks()%></h3>
        <p>Question: <%=question.getDescription()%></p>
        <p>Student's Answer: </p>
    <%
            if (question instanceof MultipleChoiceQuestion) {
                List<Choice> choices = ((MultipleChoiceQuestion) question).getChoices();
                for (Choice choice : choices) {
                    String selectionPrepend = "";
                    if (choice.getChoiceNumber() == answer.getChoiceNumber()) {
                        selectionPrepend = "->";
                    }
    %>
        <p><%=selectionPrepend%> <%=choice.getChoiceNumber()%>. <%=choice.getChoiceDescription()%></p>
    <%
                }
            } else if (question instanceof ShortAnswerQuestion) {
                String shortAnswer = answer.getShortAnswer();
    %>
        <p><%=shortAnswer%></p>
    <%
            }
        }
    %>
        <p>Fudge points: <input name="marks" size="2" value=<%=submission.getFudgePoints()%>></p>
</body>
</html>