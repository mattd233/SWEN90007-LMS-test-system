<%@ page import="domain.Subject" %>
<%@ page import="java.util.Objects" %>
<%@ page import="db.mapper.StudentMapper" %>
<%@ page import="domain.Exam" %>
<%@ page import="db.mapper.SubjectMapper" %><%--
  Created by IntelliJ IDEA.
  User: wyr04
  Date: 2020/9/22
  Time: 19:04
  To change this template use File | Settings | File Templates.
--%>
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
    Welcome back, <%=studentID%>!<br/>
    <table border="1">
        <tr>
            <th>Subject Code</th>
            <th>Subject Name</th>
            <th>Exam ID and Title</th>
        </tr>

        <%
            for (Subject subject: Objects
                    .requireNonNull(StudentMapper
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
                            .requireNonNull(SubjectMapper
                                    .getAllExamsWithSubject(subject_code))){
                        int exam_id = exam.getExamID();
                        String title = exam.getTitle();
                        String description = exam.getDescription();
                        Exam.ExamStatus status = exam.getStatus();
                %>
                <ul>
                    <li><a href="studentViewExams.jsp?studentID=<%=studentID%>&subject_code=<%=subject_code%>&exam_id=<%=exam.getExamID()%>&title=<%=exam.getTitle()%>&description=<%=exam.getDescription()%>&status=<%=exam.getStatus()%>"><%=exam_id%> <%=title%></a></li>
                </ul>
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
