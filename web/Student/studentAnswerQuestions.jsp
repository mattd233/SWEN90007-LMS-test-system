<%@ page import="db.mapper.ExamMapper" %>
<%@ page import="db.mapper.QuestionMapper" %>
<%@ page import="domain.*" %>
<%@ page import="java.security.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="static db.mapper.ChoiceMapper.getChoices" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Time" %>

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
    <title>Student answering questions </title>
    <style>
        html, body{
            width: 100%;
            height: 100%;
            overflow:hidden;
        }
        body,h3,h4{
            margin: 0;
            padding: 0;
        }
        .menu{
            margin-left: 80%;
            height: 100%;
            position:absolute;
            padding-right: 1px;
            padding-left: 1px;
            border-left: 1px solid #E6E6E6;
            border-top: 1px solid #D9D9D9;
            overflow: auto;
        }

        .contentContainer{
            width: 80%;
            height: 100%;
            position: absolute;
            overflow: auto;
        }
        .contentContainer .header{
            text-align: center;
            width: 100%;
            height: 10%;
            position: absolute;
            overflow: auto;
        }
        .contentContainer .container{
            margin-top: 10%;
            height: 70%;
            margin-left: 5%;
            width: 90%;
            position: absolute;
            overflow: auto;
        }
        .contentContainer .footer{
            bottom: 100px;
            margin-left: 10%;
            width: 70%;
            position: absolute;
            overflow: auto;
        }

    </style>
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

    String questionIndex = request.getParameter("question_index");
    int index = Integer.parseInt(questionIndex);

    // get the question list
    List<Question> questionList = QuestionMapper.getAllQuestionsWithExamID(exam_id);
    Question question = questionList.get(index);
    // get this question information
    assert question != null;
    String question_title = question.getTitle();
    String description = question.getDescription();
    int question_marks = question.getMarks();
    int question_number = question.getQuestionNumber();
%>

<%--use div to develop block layout--%>
<%--block 2: question list, time elapsed--%>
<div class ="menu">
    <h3>Question list:<br/></h3>
    <ol>
        <%
            for (int i = 0; i<questionList.size(); i++){
                String menu_title = questionList.get(i).getTitle();
        %>

        <li>
            <a href="studentAnswerQuestions.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&question_index=<%=i%>">
                <%=menu_title%>
            </a>
        </li>
        <%
            } // end for
        %>
    </ol>

</div>


<div class="contentContainer">
    <%--block 1: subject_code, start time--%>
    <div class ="header">
        <%--display the subject code and exam title--%>
        <h2><%=subject_code%> <%=title%></h2>
    </div>

    <%--block 3: display the quesion and answering textbox--%>
    <div class = "container">
        <div style="background-color:#eeeeee">
            <div style="margin-left: 2%">
                <br/><h3><%=question_title  %>  <%=question_marks%>points</h3><br/>
            </div>
        </div>
        <div id ="description" style="margin-left: 2%">
            <h4>Question Description: <%=description%></h4><br/>
            <br/>
        </div>
        <div id="answer" style="margin-left: 3%">
            <%
                if (question.getClass().equals(ShortAnswerQuestion.class)){
            %>
            <%--        // display a input textbox--%>
            <form method="post" style="width:94%;height:70%;background-color: bisque">
                input your answer in the box below<br/>
                <div align="center" >
                    <textarea style="width:80%;height:80%" name="short_answer" ></textarea>
                </div>
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
                <input type="radio" name="radioChoice" value="<%=choice.getChoiceNumber()%>">
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

    <div class ="footer" >
        <%--    previous and next question buttons--%>
        <div id="previous_next">
            <div style="float:left" class="textButton"   id="previous"  >
                <%--        get the previous question--%>
                <%
                    // if it is not the first question
                    if (index != 0){
                        int previous = index -1;
                %>
                <div>
                    <a href="studentAnswerQuestions.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&question_index=<%=previous%>">
                        <button>Previous Question</button>
                    </a>
                </div>
                <%
                    } // end if
                %>
            </div>
            <%--        get the next question--%>
            <div style="float:right" class="textButton"   id="next">
                <%
                    // if it is not the last question
                    if (index < questionList.size()-1){
                        int next = index +1;
                %>
                <div>
                    <a href="studentAnswerQuestions.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&question_index=<%=next%>">
                        <button>Next Question</button>
                    </a>
                </div>
                <%
                    } // end if
                %>
            </div>
            <br/>
        </div>

        <%--    submit quiz button--%>
        <div id="submit" style = "width:80%;float:left">
            <div style="float:right">
                <%
                    java.sql.Timestamp ts = new java.sql.Timestamp(new Date().getTime());
                %>
                <script>
                    function showTime() {
                        var date=new Date();
                        alert("The exam started at: " + date);
                    }
                </script>
                <a href="studentSubmitExams.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&ts=<%=ts%>">
                    <button onclick="showTime()">Submit Exam</button>
                </a>
            </div>
        </div>
    </div>

</body>
</html>

