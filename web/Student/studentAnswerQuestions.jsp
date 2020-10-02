<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.db.mapper.QuestionMapper" %>
<%@ page import="main.java.domain.*" %>
<%@ page import="java.util.List" %>
<%@ page import="static main.java.db.mapper.ChoiceMapper.getChoices" %>


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

        .contentContainer{
            width: 100%;
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
    List<Question> questionList = exam.getQuestions();
    Question question = questionList.get(index);
    String key = exam_id + "_" + index;
    String answer = (String) session.getAttribute(key);

    // get this question information
    assert question != null;
    String question_title = question.getTitle();
    String description = question.getDescription();
    int question_marks = question.getMarks();
    int question_number = question.getQuestionID();
//    String answer = SubmittedQuestionMapper.getAnswer(exam_id, Integer.parseInt(student_id), question_number);
%>
<%--use div to develop block layout--%>
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
        <%--        lazy load pattern is used in this area--%>

        <form method="post" action="studentSubmitExams.jsp" style="width:94%;height:70%;background-color: bisque">
            <div id="answer" style="margin-left: 3%">
                <%
                    if (question.getClass()== ShortAnswerQuestion.class){
                        answer = answer == null ? "" : answer;
                %>

                input your answer in the box below<br/>
                <div align = "center" >
                    <textarea style="width:80%;height:80%" id="short_answer" name="short_answer"><%=answer%></textarea>
                </div>

                <%
                } // end if
                else if(question.getClass()== MultipleChoiceQuestion.class){
                    // get the choices
                    List<Choice> choices = getChoices(exam_id, question_number);
                %>
                <%
                    if (answer == null){
                        for (Choice choice : choices) {
                            String choice_description = choice.getChoiceDescription();
                %>
                <%--            display all the answers--%>
                <input type="radio" name="radioChoice" value="<%=choice.getChoiceID()%>">
                <%=choice_description%><br/>
                <%
                    } // end for
                } // end if
                else {
                    int selected = Integer.parseInt(answer);
                    for (int i =0; i < choices.size(); i++){
                        Choice choice = choices.get(i);
                        String choice_description = choice.getChoiceDescription();
                        if ( i == selected){
                %>
                <input type="radio" name="radioChoice" value="<%=choice.getChoiceID()%>" checked="checked"><%=choice_description%><br/>
                <%
                } else{
                %>
                <input type="radio" name="radioChoice" value="<%=choice.getChoiceID()%>"><%=choice_description%><br/>
                <%
                                }// end else
                            } // end for
                        } // end else
                    } //end elseif
                %>
            </div>
        </form>
    </div>


    <div class ="footer" >
        <%--    previous and next question buttons--%>
        <div id="previous_next">
            <div style="float:left" class="textButton"   id="previous"  >
                <%--        get the previous question--%>
                <%
                    // if it is not the first question
                    if (index > 0){
//                        int previous = index -1;

                %>
                <div>
                    <button onclick="getPrevious()">Previous Question</button>
                    <%--                    <a href="studentAnswerQuestions.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&question_index=<%=previous%>"> </a>--%>
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
//                        int next = index +1;
                %>
                <div>
                    <button onclick="getNext()">Next Question</button>
                    <%--                    <a href="studentAnswerQuestions.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&question_index=<%=next%>"></a>--%>
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
                <%--                <a href="studentSubmitExams.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&ts=<%=ts%>">--%>
                <button onclick="submitExam()">Submit Exam</button>
                <%--                </a>--%>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        var isShortAnswer = <%=question.getClass().equals(ShortAnswerQuestion.class)%>;
        var index = "<%=index%>";
        var exam_id = "<%=exam_id%>";
        var student_id = "<%=student_id%>";
        function getNext() {
            // get parameters
            var answer = "";
            if (isShortAnswer) {
                // document.getElementById("short_answer").innerHTML = xml.responseText;
                var shortAnswer = document.getElementById("short_answer").value;
                answer = shortAnswer;
            } else {
                var radio = document.getElementsByTagName("input");
                for (var i = 0; i < radio.length; i++) {
                    if (radio[i].checked) {
                        var choiceIndex = i;
                        answer = choiceIndex;
                        // return choiceIndex;
                    }
                }
            }
            // send ajax request
            var xml = new XMLHttpRequest();
            xml.onreadystatechange = function() {
                if (xml.readyState === 4 && xml.status === 200) {
                    window.location.href = "studentAnswerQuestions.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&question_index=<%=index+1%>";
                }
            };
            xml.open("POST","/Student/studentAnswerQuestions?index=" + index + "&answer=" + answer + "&exam_id=" + exam_id ,true);
            xml.send();
        }

        function getPrevious() {
            // get parameters
            var answer = "";
            if (isShortAnswer) {
                // document.getElementById("short_answer").innerHTML = xml.responseText;
                var shortAnswer = document.getElementById("short_answer").value;
                answer = shortAnswer;
            } else {
                var radio = document.getElementsByTagName("input");
                for (var i = 0; i < radio.length; i++) {
                    if (radio[i].checked) {
                        var choiceIndex = i;
                        answer = choiceIndex;
                        // return choiceIndex;
                    }
                }
            }
            // send ajax request
            var xml = new XMLHttpRequest();
            xml.onreadystatechange = function() {
                if (xml.readyState === 4 && xml.status === 200) {
                    window.location.href = "studentAnswerQuestions.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>&question_index=<%=index-1%>";
                }
            };
            xml.open("POST","/Student/studentAnswerQuestions?index=" + index + "&answer=" + answer + "&exam_id=" + exam_id ,true);
            xml.send();
        }

        function submitExam(){
            // get parameters
            var answer = "";
            if (isShortAnswer) {
                // document.getElementById("short_answer").innerHTML = xml.responseText;
                var shortAnswer = document.getElementById("short_answer").value;
                answer = shortAnswer;
            } else {
                var radio = document.getElementsByTagName("input");
                for (var i = 0; i < radio.length; i++) {
                    if (radio[i].checked) {
                        var choiceIndex = i;
                        answer = choiceIndex;
                        // return choiceIndex;
                    }
                }
            }
            var xml = new XMLHttpRequest();
            xml.onreadystatechange = function() {
                if (xml.readyState === 4 && xml.status === 200) {
                    window.location.href = "studentSubmitExams.jsp?studentID=<%=student_id%>&exam_id=<%=exam_id%>";
                }
            };
            xml.open("POST", "/Student/studentSubmitExams?exam_id=" + exam_id +"&student_id=" + student_id +"&index=" + index + "&answer=" + answer ,true);
            xml.send();
        }
    </script>

</body>
</html>

