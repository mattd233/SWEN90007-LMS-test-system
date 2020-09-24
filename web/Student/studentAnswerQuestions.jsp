<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="domain.Exam" %>
<%@ page import="db.mapper.ExamMapper" %><%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/23
  Time: 19:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }

        th {
            text-align: left;
        }
    </style>
    <title>Student start Exams</title>
</head>
<body>
<%--get parameters directly from the url--%>
<%
    String student_id = request.getParameter("studentID");
    String examID = request.getParameter("exam_id");;
    int exam_id = Integer.parseInt(examID);
    Exam exam = ExamMapper.getExamByID(exam_id);
    assert exam != null;
    String title = exam.getTitle();
    String subject_code = exam.getSubjectCode();
%>

<%--use div to develop block layout--%>
<%--block 1: subject_code, start time--%>
<div id ="container" style = "height:50px" align = "center">
    <%--display the subject code and exam title--%>
    <%=subject_code%> <%=title%><br/>
    <%--display the exam starting time--%>
    <%
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    %>
    The exam started at <%=sdf.format(date)%><br/>
</div>

<%--block 2: question list, time elapsed--%>
<div id="menu" style = "width:200px;float:right" >
    Question list:<br/>
<%--    get the questions--%>
    Time Elapsed: <br/>
</div>

<%--block 3: display the quesion and answering textbox--%>
<div id = "content" style = "width:500px;height:500px">
    Question Description<br/>
    Answer<br/>
</div>

<%--block 4: previous and next question button--%>
<div id="previous_next" style="width:500px;height:30px">
    <div style="float:left" class="textButton"   id="previous"  >
        <button name="pre">Previous Question</button>
    </div>
    <div style="float:right" class="textButton"   id="next" >
        <button name="next">Next Question</button>
    </div>
</div>

<%--block 5: submit quiz button--%>
<div style = "width:500px;float:right">
    <button name="submit" type="submit">Submit</button>
</div>



</body>
</html>
