<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.domain.Exam" %>
<%@ page import="main.java.domain.Question" %>
<%@ page import="main.java.domain.MultipleChoiceQuestion" %>
<%@ page import="main.java.db.mapper.ChoiceMapper" %>
<%@ page import="main.java.domain.Choice" %><%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/10/1
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Edit Exam</title>
    <link rel="stylesheet" href="EditExam.css">
</head>
<body>
<%
    int examID = Integer.parseInt(request.getParameter("exam_id"));
    Exam exam = ExamMapper.getExamByID(examID);
    assert exam != null : "exam does not exist";
%>
<form action="/Instructor/editExam" method="post">
    <input type="hidden" value=<%=examID%> name="exam_id">
    Exam Title: <input type="text" placeholder=<%=exam.getTitle()%>>
    </br>
    Exam Description: <input type="text" placeholder=<%=exam.getDescription()%>>
    </br>
    Status: <%=exam.getStatus()%>
    </br>
    <%
        if (db.QuestionUOW.getCurrent() == null) {
            db.QuestionUOW.newCurrent();
        }
        for (Question question : QuestionMapper.getAllQuestionsWithExamID(examID)) {
    %>
    <div id=<%="Q" + question.getQuestionNumber()%>>
        <h3>Q<%=question.getQuestionNumber()%>: <%=question.getTitle()%></h3>
        <input type="text" placeholder="Change title" name=<%="title" + question.getQuestionNumber()%>>
        <p><%=question.getDescription()%></p>
        <input type="text" placeholder="Change description" name=<%="description" + question.getQuestionNumber()%>>
        <input type="hidden" value=<%=question instanceof MultipleChoiceQuestion ? "multiple_choice" : "short-answer"%> name=<%="type" + question.getQuestionNumber()%>>
        <p>marks: <%=question.getMarks()%></p>
        <input type="number" placeholder="Change marks" name=<%="marks" + question.getQuestionNumber()%>>
        <input type="button" value="remove" onclick=deleteQuestion(<%=question.getQuestionNumber()%>)>
        </br>
            <% if (question instanceof MultipleChoiceQuestion) {
                for (Choice choice : ChoiceMapper.getChoices(examID, question.getQuestionNumber())) {
            %>
                    <p class="choice"><%="C" + choice.getChoiceNumber() + ": " + choice.getChoiceDescription()%></p>
                    <input class = "choice_input" type="text" placeholder="Change choice" name=<%="Q"+question.getQuestionNumber()+"choice"+choice.getChoiceNumber()%>>
    <%
                }
            }
    %>
    </div>
    <%
        }
    %>
    </br>
    <input type="submit" value="Save Exam">
</form>
</body>
<script>
    function deleteQuestion(qNumber) {
        var qID = "Q".concat(qNumber);
        var rem = confirm("Do you want to delete the question?")
        if(rem) {
<%--            <%db.QuestionUOW.getCurrent().registerDeleted(new Question(examID, qNumber, ));%>--%>
            document.getElementById(qID).remove();
        }
    }
</script>
</html>