<%--
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
%>
<div style="margin-top: 100px" align="center">
    <h3><%=studentID%> submitted exam <%=examID%> successfully.<br/></h3>

    <br><br>

    <h3>
        <a href = studentViewExams.jsp?studentID=<%=studentID%>&exam_id=<%=examID%>>
            View Submission.
        </a>
    </h3>
</div>

</body>
</html>
