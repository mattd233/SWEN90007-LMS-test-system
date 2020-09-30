<%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/22
  Time: 19:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Objects" %>
<%@ page import="main.java.domain.Subject" %>
<%@ page import="main.java.domain.Exam" %>
<%@ page import="main.java.db.mapper.UserMapper" %>
<%@ page import="main.java.db.mapper.SubjectMapper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student home</title>
</head>
<body>
<div align = "center">
    <%
        int studentID = 0;
        if (session.getAttribute("user_id") == null){
            response.sendRedirect("/login.jsp");
        }
        else{
            studentID = (int) session.getAttribute("user_id");
        }
    %>
    Welcome back!<br/>
    <table border="1">
        <tr>
            <th>Subject Code</th>
            <th>Subject Name</th>
            <th>Exam ID and Title</th>
            <th>View Exam</th>
        </tr>
        <tr>
            <%
                for (Subject subject: Objects
                        .requireNonNull(UserMapper
                                .getStudentEnrolledSubject(studentID))){
            %>
                <td><%=subject.getSubjectCode()%></td>
                <td><%=subject.getSubjectName()%></td>
                <td>
    <%--            one subject can have many exam
                    display the exams in one column to indicate their relationship
    --%>
                    <%
                    String subject_code = subject.getSubjectCode();
                    for (Exam exam: Objects
                            .requireNonNull(SubjectMapper
                                    .getAllExamsWithSubject(subject_code))){
                        int exam_id = exam.getExamID();
                        String title = exam.getTitle();
                        String description = exam.getDescription();
                        Exam.ExamStatus status = exam.getStatus();
                %>
                        <ul>
                            <li><%=exam_id%><%=title%></li>
                        </ul>
                <%
                    } // end for exam
                %>
                </td>
            <%
                } // end for subject
            %>
        </tr>
    </table>
</div>
</body>
</html>
