<%--
  Created by IntelliJ IDEA.
  User: jiayuli
  Date: 22/9/20
  Time: 1:15 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="db.mapper.StudentMapper" %>
<%@ page import="java.util.ArrayList" %>
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
    <title>Submissions Table View</title>
</head>
<body>
<div align="center">
    <h1>All Exam Marks of [subject_code]<%%>]</h1>
    <table style="width:70%">
        <tr>
            <th>Student ID</th>
            <th>Student Name</th>
            <%
                // get all exams
                // exam for loop starts
            %>
            <th>Placeholder exam title</th>
            <%
                // exam for look ends
            %>
            <th>Total Marks</th>
        </tr>
        <tr>
            <%
                ArrayList<Integer> sIds = StudentMapper.findStudentIDs();
                for (Integer sId : sIds) {
            %>
            <td><%=sId%></td>
            <td>Placeholder name</td>
            <%
                // submission for loop starts
            %>
            <td>Placeholder mark of exam</td>
            <%
                // submission for loop ends
            %>
            <td>Placeholder total marks</td>
        </tr>
            <%
                } // for loop
            %>
    </table>
</div>
</body>
</html>
