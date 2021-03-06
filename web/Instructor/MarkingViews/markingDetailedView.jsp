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
<%@ page import="main.java.db.mapper.SubmittedQuestionMapper" %>
<%@ page import="main.java.domain.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/styles/instructorMarkingStyles.css" type="text/css">
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

    <a href="/Instructor/submissions_table?subject_code=<%=exam.getSubjectCode()%>">
        back to table view
    </a>
    <!-- Get the submission -->
    <%
        Submission submission = SubmissionMapper.getSubmissionByIDs(examID, userID);
        if (submission == null) {
    %>
    <!-- Case 1: Student has not submission for this exam (should not happen) -->
    <div class="notification"><p>This student has no submission for this exam.</p></div>

    <!-- Case 2: Show submission information -->
    <%
        } else {
            String submissionTime = submission.getSubmissionTime().toString();
    %>
    <p>Submitted by student: <%=userID%></p>
    <p>Submission time: <%=submissionTime%></p>
    <!-- Display questions and marks -->
    <%
        // Display questions and marks
        List<Question> questions = exam.getQuestions();
        if (questions.size() == 0) {
    %>
    <!-- Show message if there's no question in this exam (should not happen) -->
    <div class="notification"><p>This exam has no question.</p></div>
    <%
        }
        for (int i=0; i<questions.size(); i++) {
            Question question = questions.get(i);
            SubmittedQuestion answer = SubmittedQuestionMapper.getSubmittedQuestion(examID, userID, question.getQuestionNumber());
            String displayMarks = "";
            if (answer.getChoiceNumber() == 0 && answer.getShortAnswer() == null) {
                displayMarks = "0.0";
            } else {
                if (answer.isMarked()) {
                    displayMarks = Float.valueOf(answer.getMarks()).toString();
                }
            }
    %>
    <form name="markingDetailedForm" method="post">
        <!-- Question title -->
        <h3>
            <%=question.getTitle()%>:
            <input type="number" name="marksQ<%=question.getQuestionNumber()%>" value="<%=displayMarks%>"
                   onsubmit="return marksNotNullValidation(<%=question.getQuestionNumber()%>)">
             out of
            <%=question.getMarks()%>
        </h3>
        <!-- Question content -->
        <p style="font-weight: bold">Question: </p><p><%=question.getDescription()%></p>
        <%
                    if (answer.getChoiceNumber() == 0 && answer.getShortAnswer() == null) {
        %>
        <!-- Case 2.1: Student did not answer the question -->
        <div class="notification"><p>Student did not answer this question.</p></div>
        <%
                    } else {
        %>
        <!-- Case 2.2: Show student's answer -->
        <p style="font-weight: bold">Student's Answer: </p>
        <!-- Case 2.2.1: Multiple choice question -->
        <%
                        // Display student's answer
                        if (question instanceof MultipleChoiceQuestion) {
        %>
        <div class="studentAnswer">
        <%
                            List<Choice> choices = ((MultipleChoiceQuestion) question).getChoices();
                            for (Choice choice : choices) {
                                String addClass = "";
                                if (choice.getChoiceNumber() == answer.getChoiceNumber()) {
                                    addClass = " class=\"selectedChoice\"";
                                }
        %>
            <p<%=addClass%>><%=choice.getChoiceNumber()%>. <%=choice.getChoiceDescription()%></p>
        <%
                            }
        %>
        </div>
        <%
                        } else if (question instanceof ShortAnswerQuestion) {
                                String shortAnswer = answer.getShortAnswer();
        %>
        <!-- Case 2.2.2: Short answer question -->
        <div class="studentAnswer">
            <p><%=shortAnswer%></p>
        <%
                        }
        %>
        </div>
        <%
                    }
                }
        %>

        <!-- Fudge points -->
        <div class="fudgePoints">
            <p>
                Fudge points:
                <input type="number" name="fudgePoints"
                       value="<%=submission.getFudgePoints()%>" onsubmit="return fpNotNullValidation()">
            </p>
        </div>
        <input type="submit" value="Update marks">
        <%
            }
        %>
    </form>

    <script>
        function marksNotNullValidation(qID) {
            var m = document.forms["markingDetailedForm"]["marksQ" + qID].value();
            if (m == "") {
                alert("Fudge points cannot be empty.");
                return false;
            }
        }
        function fpNotNullValidation() {
            var fp = document.forms["markingDetailedForm"]["fudgePoints"].value();
            if (fp == "") {
                alert("Fudge points cannot be empty.");
                return false;
            }
        }
    </script>
</body>
</html>
