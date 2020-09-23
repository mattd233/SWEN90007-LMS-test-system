<%@ page import="domain.Exam" %><%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/22
  Time: 22:40
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
    <title>Title</title>
</head>
<body align = "center">
Detailed view of the exam<br/>
<div align="center">
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

            <td>Exam status</td>
            <td>
                <%
                    if (status.equals("CLOSED")){
                %>
                    <%=status%>

                <%
                    } // end if
                %>
                <%
                    if (status.equals("PUBLISHED")){
                %>
                <a href = "studentTakeExams.jsp?studentID=<%=studentID%>&subject_code=<%=subject_code%>&exam_id=<%=exam_id%>&title=<%=title%>&description=<%=description%>"><%=status%></a>
                <%
                    } // end if
                %>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
