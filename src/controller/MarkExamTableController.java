package controller;

import db.mapper.QuestionMapper;
import db.mapper.SubmissionMapper;
import db.mapper.SubmittedQuestionMapper;
import db.mapper.UserSubjectMapper;
import domain.Question;
import domain.Student;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/submissions_table")
public class MarkExamTableController extends HttpServlet {

    public MarkExamTableController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectCode = request.getParameter("subjectCode");
        if (subjectCode != null) {
            String view = "/Instructor/MarkingViews/markingTableView.jsp";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
            requestDispatcher.forward(request, response);
        } else {
            System.out.println("Error in MarkExamTableController doGet");
            showErrorPage(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Update fudge_points in users_has_subjects table
        String subjectCode = request.getParameter("subjectCode");
        List<Student> students = UserSubjectMapper.getAllStudentsWithSubject(subjectCode);
        for (Student student : students) {
            int sID = student.getStudentID();
            String fudgePointsStr = request.getParameter("fp"+sID);
            try {
                float fudgePoints = Float.valueOf(fudgePointsStr);
                boolean updateSuccess = UserSubjectMapper.updateFudgePoints(sID, subjectCode, fudgePoints);
                if (!updateSuccess) {
                    showErrorPage(request, response);
                    return;
                }
            } catch (Exception e) {
                showErrorPage(request, response);
                return;
            }
        }
        String view = "/Instructor/MarkingViews/markingTableView.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    private void showErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/errorPage.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }
}
