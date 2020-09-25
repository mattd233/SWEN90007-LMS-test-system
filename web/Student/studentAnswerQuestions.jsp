<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="db.mapper.ExamMapper" %>
<%@ page import="java.util.List" %>
<%@ page import="db.mapper.QuestionMapper" %>
<%@ page import="domain.*" %>
<%@ page import="static db.mapper.ChoiceMapper.getChoices" %>
<%@ page import="java.util.Objects" %>

<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/23
  Time: 19:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student start Exams</title>
</head>
<body>
<%--get parameters directly from the url--%>
<%
    String student_id = request.getParameter("studentID");
    String examID = request.getParameter("exam_id");
    int exam_id = Integer.parseInt(examID);
    Exam exam = ExamMapper.getExamByID(exam_id);
    assert exam != null;
    String title = exam.getTitle();
    String subject_code = exam.getSubjectCode();

    String questionNumber = request.getParameter("question_number");
    int question_number = Integer.parseInt(questionNumber);
%>

<%--use div to develop block layout--%>
<%--block 1: subject_code, start time--%>
<div id ="header" style = "height:10%" align = "center">
    <%--display the subject code and exam title--%>
    <h2><br/><%=subject_code%> <%=title%></h2>
    <%--display the exam starting time--%>
    <%
        // ** bug: refresh every time
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    %>
    The exam started at <%=sdf.format(date)%><br/>
</div>

<%
    // get the question list
    List<Question> questionList = QuestionMapper.getAllQuestionsWithExamID(exam_id);
    // get this question information
    Question question = QuestionMapper.getQuestionWithQuestionID(exam_id, question_number);
    assert question != null;
%>

<%--block 2: question list, time elapsed--%>
<div id="menu" style = "width:300px;float:right" >
    Question list:<br/>
    <ol>
    <%
        for (int i = 0; i<questionList.size(); i++){
            String question_title = questionList.get(i).getTitle();
    %>

        <li>
            <a href="studentAnswerQuestions.jsp?student_id=<%=student_id%>&exam_id=<%=exam_id%>&question_number=<%=i+1%>">
                <%=question_title%>
            </a>
        </li>
    <%
        } // end for
    %>
    </ol>
    Time Elapsed: <br/>
</div>

<%
    String question_title = question.getTitle();
    String description = question.getDescription();
    int question_marks = question.getMarks();
%>
<%--block 3: display the quesion and answering textbox--%>
<div id = "content" style = "width:80%;height:60%">
    <div style="background-color:#eeeeee" id="title">
        <h3><%=question_title  %>  <%=question_marks%>points</h3>
    </div>
    <div id ="description">
        <h4>Question Description: <%=description%><br/></h4>
        <br/>
    </div>
    <div id="answer">
        <%
            if (question.getClass().equals(ShortAnswerQuestion.class)){
                // display a input box
        %>
<%--        // display a input textbox--%>
            <form>
                answer<br/>
            </form>
        <%
            } // end if
            else if(question.getClass().equals(MultipleChoiceQuestion.class)){
                // get the choices
                List<Choice> choices = getChoices(exam_id, question_number);
                // display the choices using form
        %>
            <form action="">
                <%
                    for (Choice choice : choices) {
                        String choice_description = choice.getChoiceDescription();
                %>
                    <input type="checkbox" value="<%=choice_description%>">
                        <%=choice_description%><br/>
                <%
                    } // end for
                %>
            </form>
        <%
            } //end elseif
        %>
    </div>

</div>

<%--block 4: footer, buttons--%>
<div id ="footer">
<%--    previous and next question buttons--%>
    <div id="previous_next" style="width:80%">
        <div style="float:left" class="textButton"   id="previous"  >
    <%--        get the previous question--%>
            <%
                // if it is not the first question
                if (question_number != 1){
                    int previous_number = question_number - 1;
            %>
                <div>
                    <a href="studentAnswerQuestions.jsp?student_id=<%=student_id%>&exam_id=<%=exam_id%>&question_number=<%=previous_number%>">
                        <button>Previous Question</button>
                    </a>
                </div>
            <%
                } // end if
            %>
        </div>
    <%--        get the next question--%>
        <div style="float:right" class="textButton"   id="previous"  >
        <%
            // if it is not the last question
            if (question_number != questionList.size()){
                int next_number = question_number + 1;
        %>
            <div>
                <a href="studentAnswerQuestions.jsp?student_id=<%=student_id%>&exam_id=<%=exam_id%>&question_number=<%=next_number%>">
                    <button>Next Question</button>
                </a>
            </div>
        <%
            } // end if
        %>
        </div>
    </div>

    <%--    submit quiz button--%>
    <div id="footer" style = "width:80%;float:left">
        <div style="float:right">
            <a href="studentSubmitExams.jsp?student_id=<%=student_id%>&exam_id=<%=exam_id%>">
                <button>Submit Exam</button>
            </a>
        </div>
    </div>
</div>


</body>
</html>