<%@ page import="main.java.domain.Exam" %>
<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="java.util.Objects" %><%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/24
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>student submit exams</title>
</head>
<body>
<%
    String studentID = request.getParameter("studentID");
    String examID = request.getParameter("exam_id");
    int exam_id = Integer.parseInt(examID);
    String title = Objects.requireNonNull(ExamMapper.getExamByID(exam_id)).getTitle();
    String status = ExamMapper.getExamStatus(exam_id);
%>
<div style="margin-top: 100px" align="center">
    <%
        assert status != null;
        if (status.equals("PUBLISHED")){
    %>
    <h3><%=studentID%> submitted <%=title%> successfully.<br/></h3>
    <%
        } // end if
        else if (status.equals("CLOSED")){
    %>
    <h3>Failed. The exam has been closed.<br/></h3>
    <%
        } // end else if
    %>

    <br><br>

    <h3>
        <a href = studentViewExams.jsp?studentID=<%=studentID%>&exam_id=<%=examID%>>
            View Submission.
        </a>
    </h3>
</div>

</body>
</html>
