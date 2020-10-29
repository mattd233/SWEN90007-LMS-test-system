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
    <link rel="stylesheet" href="/styles/instructorMarkingStyles.css" type="text/css">
    <title><%=request.getParameter("subject_code")%> Submissions Table View</title>
</head>
<body>
<div align="center">
    <h1>All Exam Submissions of <%=request.getParameter("subject_code")%></h1>
    <form name="markingTableForm" method="post">
        <table>
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
            <!-- Header row ends -->

            <!-- Show all students taking the subject and their marks as rows -->
            <%
                List<Student> students = UserSubjectMapper.getAllStudentsWithSubject(subjectCode);
                if (students.size() == 0) {
            %>
            <!-- Case 1: Show message if subject has not students -->
            <p>No student enrolled in this subject.</p>
            <%
                } else {
                    // student for loop starts
                    for (Student student : students) {
                        int uId = student.getStudentID();
            %>
            <!-- Case 2: Show students and their submission -->
            <tr>
                <!-- Student ID -->
                <td><%=uId%></td>
                <!-- Student Name -->
                <td><%=student.getName()%></td>
                <!-- Exam marks -->
                <%
                    // submission for loop starts
//                    float totalMarks = 0;
//                    boolean allSubmissionsMarked = true;
                    for (Exam exam : exams) {
                        int eId = exam.getExamID();
                        Submission submission = SubmissionMapper.getSubmissionByIDs(eId, uId);
                        if (submission == null) {
                %>
                <!-- Case 2.1: Show message if student has no submission -->
                <td>
                    <input type="number" name="m_<%=exam.getExamID()%>_<%=uId%>"><br>
                    No submission
                </td>
                <%
                        } else if (submission.isMarked()) {
                            float marks = submission.getMarks();
//                            totalMarks += marks;
                %>
                <!-- Case 2.2: Show marks with link to detailed view if there is a submission and it's marked -->
                <td>
                    <input type="number" name="m_<%=exam.getExamID()%>_<%=uId%>" value="<%=marks%>"><br>
                    <a href="/submissions_detail?examID=<%=eId%>&userID=<%=uId%>">Mark this exam</a><br>
                    (Finished marking)
                </td>
                <%
                        } else {
                            float marks = submission.getMarks();
                            String displayMarks = Float.valueOf(marks).toString();
                            if (marks == -1) {
                                displayMarks = "";
                            }
//                            allSubmissionsMarked = false;
                %>
                <!-- Case 2.3: Show link to detailed view if there is a submission but not marked -->
                <td>
                    <input type="number" name="m_<%=exam.getExamID()%>_<%=uId%>" value="<%=displayMarks%>"><br>
                    <a href="/submissions_detail?examID=<%=eId%>&userID=<%=uId%>">Mark this exam</a>
                </td>
                <%
                        }
                    } // submission for loop ends
                    float fudgePoints = UserSubjectMapper.getFudgePoints(uId, subjectCode);
//                    float finalMarks = totalMarks + fudgePoints;
                %>

                <!-- Fudge point -->
                <td>
                    <input type="number" name="fp<%=uId%>" value="<%=fudgePoints%>">
                </td>
                <%
                    String displayFinalMarks = "N/A";
//                    if (allSubmissionsMarked) {
//                        displayFinalMarks = Float.valueOf(finalMarks).toString();
//                    }
                    float finalMarks = UserSubjectMapper.getMarks(uId, subjectCode);
                    if (finalMarks != -1) {
                        displayFinalMarks = Float.valueOf(finalMarks).toString();
                    }
                %>

                <!-- Total marks -->
                <td><%=displayFinalMarks%></td>

            </tr>
            <%
                    } // student for loop ends
                }
            %>
        </table>
        <!---->
        <input class="submitButton" type="submit" value="Update marks">
    </form>
    <a href="/Instructor/instructorSubjects.jsp">
        back to subject page
    </a><br>
    <a href="/Instructor/instructorExams.jsp?subject_code=<%=subjectCode%>">
        back to the exam page of subject <%=subjectCode%>
    </a>
</div>
<script>
    function notNullValidation(uId) {
        var s = document.forms["markingTableForm"]["fp"+uId.toString()].value;
        if (s == "" || s == null) {
            alert("Fudge points cannot be empty.");
            return false;
        }
        return true;
    }
</script>
</body>
</html>
