<%--
  Created by IntelliJ IDEA.
  User: Matt
  Date: 2020/9/21
  Time: 22:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create an exam</title>
</head>
<body>
<form name="CreateExam" action="/Instructor/createNewExam" method="post" id="form">
    Title: <input type="text"  placeholder="Enter title here." name="exam_title" id="title">
    <br>
    Description:
    <br>
    <textarea placeholder="Enter description here." name="exam_description" rows="5" cols="100" id="description"></textarea>
    <br>
    <input type="hidden" name="subject_code" value="<%=request.getParameter("subject_code")%>">
    <fieldset id="add_exam_questions">
        <legend>Add new questions!</legend>
    </fieldset>
    <input type="button" value="Add a short-answer question" class="add" id="short-answer" />
    <input type="button" value="Add a multiple-choice question" class="add" id="multiple-choice" />
    <input type="button" value="Goback" onclick=window.location.replace("/Instructor/instructorExams.jsp?subject_code=<%=request.getParameter("subject_code")%>");>
    <input type="button" value="Create exam" onclick=saveExam()>
</form>
</body>
<script>
    function saveExam(){
        var emptyField = false;
        var titles = document.getElementsByClassName("fieldtitle");
        var descriptions = document.getElementsByClassName("fielddescription");
        var marks = document.getElementsByClassName("fieldmarks");
        var choices = document.getElementsByClassName("fieldchoices");
        for (i = 0; i < titles.length; i++) {
            if (titles[i].value.length===0) {
                emptyField = true;

            }
            if (descriptions[i].value.length===0) {
                emptyField = true;
            }
            if (marks[i].value.length===0) {
                emptyField = true;
            }
        }
        for (i=0; i<choices.length; i++) {
            if (choices[i].value.length===0) {
                emptyField = true;
            }
        }

        var title =  document.getElementById("title").value;
        var description =  document.getElementById("description").value;
        if (title.length===0) {
            window.alert("Please enter a title.")
        }
        else if (description.length===0) {
            window.alert("Please enter a description.")
        }
        else if (document.getElementById("field1")==null) {
            window.alert("Please add at least one question.")
        }
        else if (emptyField) {
            window.alert("Empty fields in the exam, please check before submit.")
        }
        else {
            document.getElementById("form").submit();
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
            var fType = $("<input type=\"hidden\" name=\"type" + intId + "\" value=\"short_answer\"/>");
            var fTitle = $("<input type=\"text\" placeholder =\"title\" class=\"fieldtitle\" name=\"title" + intId + "\" />");
            var fDescription = $("<input type=\"text\" placeholder =\"description\" size = \"80\" class=\"fielddescription\" name=\"description" + intId + "\" />");
            var fMarks = $("<input type=\"number\" placeholder =\"marks\" class=\"fieldmarks\" name=\"marks" + intId + "\" />");
            var removeButton = $("<input type=\"button\" class=\"remove\" value=\"-\" />");
            removeButton.click(function() {
                $(this).parent().remove();
            });

            fieldWrapper.append(intId + ". ");
            fieldWrapper.append(fType);
            fieldWrapper.append(fTitle);
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
            var fType = $("<input type=\"hidden\" name=\"type" + intId + "\" value=\"multiple_choice\"/>");
            var fTitle = $("<input type=\"text\" placeholder =\"title\" class=\"fieldtitle\" name=\"title" + intId + "\" />");
            var fDescription = $("<input type=\"text\" placeholder =\"description\" size=\"80\" class=\"fielddescription\" name=\"description" + intId + "\" />");
            var fMarks = $("<input type=\"number\" placeholder =\"marks\" class=\"fieldmarks\" name=\"marks" + intId + "\" />");
            var choice1 = $("<br>    <input type=\"text\" size=\"50\" class=\"fieldchoices\" placeholder=\"Q" + intId + "choice" + 1 + "\" name=\"Q" + intId + "choice" + 1 + "\" /><br>");
            var choice2 = $("<br>    <input type=\"text\" size=\"50\" class=\"fieldchoices\" placeholder=\"Q" + intId + "choice" + 2 + "\" name=\"Q" + intId + "choice" + 2 + "\" /><br>");
            var addChoiceButton = $("<input type=\"button\" class=\"fieldchoice\" value=\"Add a choice\" />");
            addChoiceButton.click(function () {
                var id = (fieldWrapper.data("choice_idx") + 3 || 3);
                $(this).parent().append("<br>    <input type=\"text\" size=\"50\" class=\"fieldchoices\" placeholder=\"Q" + intId + "choice" + id + "\" name=\"Q" + intId + "choice" + id + "\" /><br>");
                fieldWrapper.data("choice_idx", id);
            });
            var removeButton = $("<input type=\"button\" class=\"remove\" value=\"-\" />");
            removeButton.click(function() {
                $(this).parent().remove();
            });

            fieldWrapper.append(intId + ". ");
            fieldWrapper.append(fType);
            fieldWrapper.append(fTitle);
            fieldWrapper.append(fDescription);
            fieldWrapper.append(fMarks);
            fieldWrapper.append(removeButton);
            fieldWrapper.append(addChoiceButton);
            fieldWrapper.append(choice1);
            fieldWrapper.append(choice2)
            $("#add_exam_questions").append(fieldWrapper);
        });
    });
</script>
</html>
