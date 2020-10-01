<%--
  Created by IntelliJ IDEA.
  User: jiayuli
  Date: 22/9/20
  Time: 1:15 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="main.java.db.mapper.ExamMapper" %>
<%@ page import="main.java.db.mapper.SubmissionMapper" %>
<%@ page import="main.java.db.mapper.UserSubjectMapper" %>
<%@ page import="main.java.domain.Exam" %>
<%@ page import="main.java.domain.Submission" %>
<%@ page import="main.java.domain.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/Instructor/MarkingViews/markingStyles.css" type="text/css">
    <title><%=request.getParameter("subject_code")%> Submissions Table View</title>
</head>
<body>
<div align="center">
    <form method="post">
        <h1>All Exam Submissions of <%=request.getParameter("subject_code")%></h1>
        <table style="width:70%">
            <!-- Header row -->
            <tr>
                <th>Student ID</th>
                <th>Student Name</th>
                <!-- Show all exam titles as column headers -->
                <%
                    String subjectCode = request.getParameter("subject_code");
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
            <%
                List<Student> students = UserSubjectMapper.getAllStudentsWithSubject(subjectCode);
                if (students.size() == 0) {
            %>
            <p>No student enrolled in this subject.</p>
            <%
                } else {
                    // student for loop starts
                    for (Student student : students) {
                        int uId = student.getStudentID();
            %>
            <tr>
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
            </tr>
            <%
                    } // student for loop ends
                }
            %>
        </table>
        <input type="submit" value="Update marks">
    </form>
</div>
</body>
</html>
