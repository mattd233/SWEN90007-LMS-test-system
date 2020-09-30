<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/22
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.domain.Exam" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body align = "center">
Detailed view of the exam<br/>

<%--    add a link to get back to homepage--%>
<a href="javascript:history.go(-1);">Go back</a>

    <table border="1" align = "center">

<%--get parameters directly from the url--%>
<%
    String studentID = request.getParameter("studentID");
    String exam_id = request.getParameter("exam_id");
    String title = request.getParameter("title");
    String subject_code = request.getParameter("subject_code");
    String description = request.getParameter("description");
    String status = request.getParameter("status");
%>

<%--    fill the table--%>
        <tr>
            <td>Exam id</td>
            <td><%=exam_id%></td>
        </tr>
        <tr>
            <td>Exam title</td>
            <td><%=title%></td>
        </tr>
        <tr>
            <td>Subject code</td>
            <td><%=subject_code%></td>
        </tr>
        <tr>
            <td>Exam description</td>
            <td><%=description%></td>
        </tr>
<%--    if the exam is published, the student can take the exam--%>
        <tr>

            <td>Exam status (Note that you can only take PUBLISHED exam)</td>
            <td>
                <%
                    if (status.equals("UNPUBLISHED") || status.equals("CLOSED")){
                %>
                    <%=status%>
<%--                <a href = "studentTakeExams.jsp?studentID=<%=studentID%>&examID=<%=exam_id%>"><%=status%></a>--%>
                <%
                    } // end if
                %>
                <%
                    if (status.equals("PUBLISHED")){
                %>
                <a href = "studentTakeExams.jsp?studentID=<%=studentID%>&examID=<%=exam_id%>"><%=status%></a>
                <%
                    } // end if
                %>
<%--                <%=status%>--%>
<%--                <%--%>
<%--                    } // end else--%>
<%--                %>--%>
            </td>
        </tr>
    </table>

</body>
</html>
