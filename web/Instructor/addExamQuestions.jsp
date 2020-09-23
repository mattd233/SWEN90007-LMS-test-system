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
    <title>Title</title>
</head>

<body>
<form action="/Instructor/saveExam" method="post">
<fieldset id="add_exam_questions">
    <legend>Add new questions!</legend>
</fieldset>
<input type="button" value="Add a short-answer question" class="add" id="short-answer" />
<input type="button" value="Add a multiple-choice question" class="add" id="multiple-choice" />
<input type="submit" value="Save the exam"/>
<input type="hidden" name="exam_id" value="<%=request.getParameter("exam_id")%>">
</form>
</body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
<script>
    $(document).ready(function() {
        // add a short-answer question
        $("#short-answer").click(function() {
            var lastField = $("#add_exam_questions div:last");
            var intId = (lastField && lastField.length && lastField.data("idx") + 1) || 1;
            var fieldWrapper = $("<div class=\"fieldwrapper\" id=\"field" + intId + "\" />");
            fieldWrapper.data("idx", intId);
            var fType = $("<input type=\"hidden\" name=\"type" + intId + "\" value=\"short_answer\"/>");
            var fName = $("<input type=\"text\" placeholder =\"title\" class=\"fieldname\" name=\"title" + intId + "\" />");
            var fDescription = $("<input type=\"text\" placeholder =\"description\" class=\"fieldname\" name=\"description" + intId + "\" />");
            var fMarks = $("<input type=\"number\" placeholder =\"marks\" class=\"fieldmarks\" name=\"marks" + intId + "\" />");
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
            var fType = $("<input type=\"hidden\" name=\"type" + intId + "\" value=\"multiple_choice\"/>");
            var fName = $("<input type=\"text\" placeholder =\"title\" class=\"fieldname\" name=\"title" + intId + "\" />");
            var fDescription = $("<input type=\"text\" placeholder =\"description\" class=\"fielddecription\" name=\"description" + intId + "\" />");
            var fMarks = $("<input type=\"number\" placeholder =\"marks\" class=\"fieldmarks\" name=\"marks" + intId + "\" />");
            var addChoiceButton = $("<input type=\"button\" class=\"fieldchoice\" value=\"Add a choice\" />");
            addChoiceButton.click(function() {
                var id = (fieldWrapper.data("choice_idx") + 1) || 1;
                $(this).parent().append("<input type=\"text\" placeholder=\"Q" + intId + "choice"  + id + "\" name=\"Q" + intId + "choice"  + id + "\" />");
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
