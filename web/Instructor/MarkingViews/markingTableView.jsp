<%--
  Created by IntelliJ IDEA.
  User: jiayuli
  Date: 22/9/20
  Time: 1:15 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="db.mapper.ExamMapper" %>
<%@ page import="domain.Exam" %>
<%@ page import="db.mapper.SubmissionMapper" %>
<%@ page import="domain.Submission" %>
<%@ page import="domain.Student" %>
<%@ page import="db.mapper.UserSubjectMapper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/Instructor/MarkingViews/markingStyles.css" type="text/css">
    <title><%=request.getParameter("subjectCode")%> Submissions Table View</title>
</head>
<body>
<div align="center">
    <form method="post">
        <h1>All Exam Submissions of <%=request.getParameter("subjectCode")%></h1>
        <table style="width:70%">
            <!-- Header row -->
            <tr>
                <th>Student ID</th>
                <th>Student Name</th>
                <!-- Show all exam titles as column headers -->
                <%
                    String subjectCode = request.getParameter("subjectCode");
                    List<Exam> exams = ExamMapper.getAllExamsWithSubjectCode(subjectCode);
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

            <!-- Show all students taking the subject and their marks as rows -->
            <tr>
                <%
                    // student for loop starts
                    List<Student> students = UserSubjectMapper.getAllStudentsWithSubject(subjectCode);
                    for (Student student : students) {
                        int uId = student.getStudentID();
                        %>
                        <!-- Student ID -->
                        <td><%=uId%></td>
                        <!-- Student Name -->
                        <td><%=student.getName()%></td>
                        <!-- Exam marks -->
                        <%
                        // submission for loop starts
                        float totalMarks = 0;
                        boolean allSubmissionsMarked = true;
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
                                <td><a href="/submissions_detail?examID=<%=eId%>&userID=<%=uId%>"><%=marks%></a></td>
                        <%
                            } else {
                                allSubmissionsMarked = false;
                        %>
                                <td><a href="/submissions_detail?examID=<%=eId%>&userID=<%=uId%>">Mark this exam</a></td>
                        <%
                            }
                        } // submission for loop ends
                        float fudgePoints = UserSubjectMapper.getFudgePoints(uId, subjectCode);
                        float finalMarks = totalMarks + fudgePoints;
                    %>
                    <td><input name="fp<%=uId%>" size="5" value="<%=fudgePoints%>"></td>
                    <%
                        String displayFinalMarks = "N/A";
                        if (allSubmissionsMarked) {
                            displayFinalMarks = Float.valueOf(finalMarks).toString();
                        }
                    %>
                        <td><%=displayFinalMarks%></td>
                    <%
                    } // student for loop ends
                %>
            </tr>
        </table>
        <input type="submit" value="Update marks">
    </form>
</div>
</body>
</html>
