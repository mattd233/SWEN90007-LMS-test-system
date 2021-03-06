<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/22
  Time: 19:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.db.mapper.UserMapper" %>
<%@ page import="main.java.domain.Exam" %>
<%@ page import="main.java.domain.Student" %>
<%@ page import="main.java.domain.Subject" %>
<%@ page import="java.util.Objects" %>
<%@ page import="main.java.db.mapper.SubjectMapper" %>
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
    <title>Student home</title>
</head>
<body>
<%--<div align="right">--%>
<%--    <form name="LogoutForm" action="/logout.jsp" method="post">--%>
<%--        <input type="submit" value="Logout"/>--%>
<%--    </form>--%>
<%--</div>--%>
<div align="right">
    <button onclick="logout()">Logout</button>
</div>

<script>
    function logout(){
        alert("Logout successfully");
        window.location.href='/logout.jsp';
    }
</script>
<div align="center">

    <%
        int studentID = 0;
        if (session.getAttribute("user_id") == null){
            response.sendRedirect("/login.jsp");
        }
        else{
            studentID = (int) session.getAttribute("user_id");
        }
    %>

    <%--header--%>
    <h1>Welcome back, <shiro:principal/>!</h1>

    <table>
        <tr>
            <th>Subject Code</th>
            <th>Subject Name</th>
            <th>Exam ID and Title</th>
        </tr>

        <%
            for (Subject subject: Objects
                    .requireNonNull(SubjectMapper
                            .getStudentEnrolledSubject(studentID))){
        %>
        <tr>
            <td><%=subject.getSubjectCode()%></td>
            <td><%=subject.getSubjectName()%></td>
            <td>
                <%--            one subject can have many exam
                                display the exams in one column to indicate their relationship
                                link to viewexam page
                --%>
                <%
                    String subject_code = subject.getSubjectCode();
                    for (Exam exam: Objects
                            .requireNonNull(ExamMapper
                                    .getAllExamsWithSubjectCode(subject_code))){
                        int exam_id = exam.getExamID();
                        String title = exam.getTitle();
                        Exam.ExamStatus status = exam.getStatus();
                %>
                    <%
                        // select the available exam for student to view
                        // status = 'PUBLISHED' or 'CLOSED'
                        if (status == Exam.ExamStatus.PUBLISHED || status == Exam.ExamStatus.CLOSED ) {%>
                    <ul>
                        <li>
                            <a href="studentViewExams.jsp?studentID=<%=studentID%>&exam_id=<%=exam_id%>"><%=exam_id%> <%=title%>
                            </a></li>
                    </ul>
                    <%
                        } // end if
                    %>

                <%
                    } // end for exam
                %>
            </td>

        </tr>
        <%
            } // end for subject
        %>

    </table>
</div>
</body>
</html>
