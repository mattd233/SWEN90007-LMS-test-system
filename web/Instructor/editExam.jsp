<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.db.mapper.QuestionMapper" %>
<%@ page import="main.java.db.mapper.ChoiceMapper" %>
<%@ page import="main.java.db.QuestionUOW" %>
<%@ page import="main.java.domain.*" %>
<%--
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
    <style>
        .choice {
            margin-top: 10px;
            margin-left: 20px;
            white-space: nowrap;
        }

        .choice_input {
            margin-top: 10px;
            margin-left: 20px;
        }


    </style>
</head>
<body>
<%
    int examID = Integer.parseInt(request.getParameter("exam_id"));
    Exam exam = ExamMapper.getExamByID(examID);

%>
<form action="/Instructor/editExam" method="post">
    <input type="hidden" value=<%=examID%> name="exam_id", id="exam_id">
    <h3>Title: <%=exam.getTitle()%></h3>
    <input type="text" placeholder="Change title" name="exam_title">
    <p>Description: <%=exam.getDescription()%></p>
    <textarea placeholder="Change description" name="exam_description" rows="5" cols="100"></textarea>
    <p>Status: <%=exam.getStatus()%></p>
    <br>
    <%
        for (Question question : QuestionMapper.getAllQuestionsWithExamID(examID)) {
    %>
        <div id=<%="Q" + question.getQuestionNumber()%>>
            <h3 class="title"><%=question.getTitle()%></h3>

            <input type="text" placeholder="Change title" name=<%="title" + question.getQuestionNumber()%>>
            <p><%=question.getDescription()%></p>
            <input type="text" class="description" size="100" placeholder="Change description" name=<%="description" + question.getQuestionNumber()%>>
            <input type="hidden" value=<%=question instanceof MultipleChoiceQuestion ? "multiple_choice" : "short-answer"%> name=<%="type" + question.getQuestionNumber()%>>
            <p>marks: <%=question.getMarks()%></p>
            <input type="number" placeholder="Change marks" name=<%="marks" + question.getQuestionNumber()%>>

            <br>
                <% if (question instanceof MultipleChoiceQuestion) {
                    for (Choice choice : ChoiceMapper.getChoices(examID, question.getQuestionNumber())) {
                %>
                        <p class="choice"><%="C" + choice.getChoiceNumber() + ": " + choice.getChoiceDescription()%></p>
                        <input class = "choice_input" size="50" type="text" placeholder="Change choice" name=<%="Q"+question.getQuestionNumber()+"choice"+choice.getChoiceNumber()%>>
        <%
                    }
        %>
                        <br>
        <%
                }
        %>
            <input type="button" value="remove" onclick=deleteQuestion(<%=question.getQuestionNumber()%>)>
            <br>
        </div>
    <%
        }
    %>
    <br>
    <fieldset id="add_exam_questions">
        <legend>Add new questions!</legend>
    </fieldset>
    <input type="button" value="Add a short-answer question" class="add" id="short-answer" />
    <input type="button" value="Add a multiple-choice question" class="add" id="multiple-choice" />
    <input type="button" value="Go back" onclick=window.location.replace("/Instructor/instructorExams.jsp?subject_code=<%=exam.getSubjectCode()%>");>
    <input type="submit" value="Save Exam">
</form>
</body>
<script>
    var exam_id = <%=examID%>;
    function deleteQuestion(qNumber) {
        var qID = "Q".concat(qNumber);
        var rem = confirm("Do you want to delete the question?");
        if(rem) {
            document.getElementById(qID).remove();
            var target = "editExam?exam_id=".concat(exam_id, "&deleteQuestion=", qNumber);
            window.location.replace(target);
        }
    }
</script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<script>
<%--  This script is based on the following answer by FarligOpptreden on stack overflow:
https://stackoverflow.com/questions/9173182/add-remove-input-field-dynamically-with-jquery
modified by Simai Deng 10/2020  --%>
    $(document).ready(function() {
        // add a short-answer question
        $("#short-answer").click(function() {
            var lastField = $("#add_exam_questions div:last");
            var intId = (lastField && lastField.length && lastField.data("idx") + 1) || 1;
            var fieldWrapper = $("<div class=\"fieldwrapper\" id=\"field" + intId + "\" />");
            fieldWrapper.data("idx", intId);
            var fType = $("<input type=\"hidden\" name=\"new_type" + intId + "\" value=\"short_answer\"/>");
            var fName = $("<input type=\"text\" placeholder =\"title\" class=\"fieldname\" name=\"new_title" + intId + "\" />");
            var fDescription = $("<input type=\"text\" placeholder =\"description\" size=\"80\" class=\"fieldname\" name=\"new_description" + intId + "\" />");
            var fMarks = $("<input type=\"number\" placeholder =\"marks\" class=\"fieldmarks\" name=\"new_marks" + intId + "\" />");
            var removeButton = $("<input type=\"button\" class=\"remove\" value=\"-\" />");
            removeButton.click(function() {
                $(this).parent().remove();
            });

            fieldWrapper.append(intId + ". ");
            fieldWrapper.append(fType);
            fieldWrapper.append(fName);
            fieldWrapper.append(fDescription);
            fieldWrapper.append(fMarks);
            fieldWrapper.append(removeButton);
            $("#add_exam_questions").append(fieldWrapper);
        });
        // add a multiple-choice question
        $("#multiple-choice").click(function() {
            var lastField = $("#add_exam_questions div:last");
            var intId = (lastField && lastField.length && lastField.data("idx") + 1) || 1;
            var fieldWrapper = $("<div class=\"fieldwrapper\" id=\"field" + intId + "\" />");
            fieldWrapper.data("idx", intId);
            var fType = $("<input type=\"hidden\" name=\"new_type" + intId + "\" value=\"multiple_choice\"/>");
            var fName = $("<input type=\"text\" placeholder =\"title\" class=\"fieldname\" name=\"new_title" + intId + "\" />");
            var fDescription = $("<input type=\"text\" placeholder =\"description\" size=\"80\" class=\"fielddecription\" name=\"new_description" + intId + "\" />");
            var fMarks = $("<input type=\"number\" placeholder =\"marks\" class=\"fieldmarks\" name=\"new_marks" + intId + "\" />");
            var addChoiceButton = $("<input type=\"button\" class=\"fieldchoice\" value=\"Add a choice\" />");
            addChoiceButton.click(function() {
                var id = (fieldWrapper.data("choice_idx") + 1) || 1;
                $(this).parent().append("<br>    <input type=\"text\" size=\"50\" placeholder=\"new_Q" + intId + "choice"  + id + "\" name=\"Q" + intId + "choice"  + id + "\" /><br>");
                fieldWrapper.data("choice_idx", id);
            });
            var removeButton = $("<input type=\"button\" class=\"remove\" value=\"-\" />");
            removeButton.click(function() {
                $(this).parent().remove();
            });

            fieldWrapper.append(intId + ". ");
            fieldWrapper.append(fType);
            fieldWrapper.append(fName);
            fieldWrapper.append(fDescription);
            fieldWrapper.append(fMarks);
            fieldWrapper.append(removeButton);
            fieldWrapper.append(addChoiceButton);
            fieldWrapper.append(addChoiceButton);
            $("#add_exam_questions").append(fieldWrapper);
        });
    });
</script>
</html>
