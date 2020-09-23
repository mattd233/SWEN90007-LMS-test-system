<%--
  Created by IntelliJ IDEA.
  User: jiayuli
  Date: 22/9/20
  Time: 1:15 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="db.mapper.StudentMapper" %>
<%@ page import="db.mapper.ExamMapper" %>
<%@ page import="domain.Exam" %>
<%@ page import="db.mapper.SubmissionMapper" %>
<%@ page import="domain.Submission" %>
<%@ page import="domain.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 5px;
        }
        th {
            text-align: left;
        }
    </style>
    <title><%=request.getParameter("subject")%> Submissions in Table View</title>
</head>
<body>
<div align="center">
    <h1>All Exam Submissions of <%=request.getParameter("subject")%></h1>
    <table style="width:70%">
        <tr>
            <th>Student ID</th>
            <th>Student Name</th>
            <!-- Exam titles -->
            <%
                List<Exam> exams = ExamMapper.getAllExamsWithSubjectCode(request.getParameter("subject"));
                for (Exam exam : exams) {
                    String title = exam.getTitle();
                // exam for loop starts
            %>
            <th><%=title%></th>
            <%
                } // exam for look ends
            %>
            <th>Fudge Points</th>
            <th>Total Marks</th>
        </tr>
        <tr>
            <%
                // student for loop starts
                List<Student> students = StudentMapper.getAllStudents();
                for (Student student : students) {
                    int uId = student.getStudentID();
                    String sName = student.getName();
            %>
            <!-- Student ID -->
            <td><%=uId%></td>
            <!-- Student Name -->
            <td><%=sName%></td>
            <!-- Exam marks -->
            <%
                // submission for loop starts
                float totalMarks = 0;
                String subjectCode = request.getParameter("subject");
                for (Exam exam : exams) {
                    int eId = exam.getExamID();
                    Submission submission = SubmissionMapper.getSubmissionByIDs(eId, uId);
                    if (submission == null) {
            %>
            <td>No submission</td>
            <%
                    } else if (submission.isMarked()){
                        float marks = submission.getMarks();
                        totalMarks += marks;
            %>
            <td><a href="/submissions?subject=<%=subjectCode%>&examID=<%=eId%>&userID=<%=uId%>"><%=marks%></a></td>
            <%
                    } else {
            %>
            <td><a href="/submissions?subject=<%=subjectCode%>&examID=<%=eId%>&userID=<%=uId%>">Mark this exam</a></td>
            <%
                    }
                } // submission for loop ends
            %>
            <td><input name="fudgePoints" size="3"></td>
            <td><%=totalMarks%></td>
        </tr>
            <%
                } // student for loop ends
            %>
    </table>
</div>
</body>
</html>
