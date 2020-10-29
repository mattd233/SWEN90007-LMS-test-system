package main.java.controller.instructor;

import main.java.db.mapper.ExamMapper;
import main.java.db.mapper.SubmissionMapper;
import main.java.db.mapper.UserSubjectMapper;
import main.java.domain.Exam;
import main.java.domain.Student;
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
        String subjectCode = request.getParameter("subject_code");
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
        // TODO: Check lock
        // TODO: If lock available:
        String subjectCode = request.getParameter("subject_code");
        List<Student> students = UserSubjectMapper.getAllStudentsWithSubject(subjectCode);
        List<Exam> exams = ExamMapper.getAllExamsWithSubjectCode(subjectCode);
        for (Student student : students) {
            int sID = student.getStudentID();
            // Update marks
            for (Exam exam : exams) {
                String marksStr = request.getParameter("m_"+exam.getExamID()+"_"+sID);
                System.out.println(marksStr);
                try {
                    float marks = Float.valueOf(marksStr);
                    SubmissionMapper.updateSubmissionMarks(exam.getExamID(), sID, marks);
                } catch (Exception e) {
                    continue;
                }
            }

            // Update fudge_points in users_has_subjects table
            String fudgePointsStr = request.getParameter("fp"+sID);
            try {
                float fudgePoints = Float.valueOf(fudgePointsStr);
                boolean updateSuccess = UserSubjectMapper.updateFudgePoints(sID, subjectCode, fudgePoints);
                if (!updateSuccess) {
                    System.err.println("Error in MarkExamTableController doPost: Update not successful");
                    showErrorPage(request, response, "Update not successful. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorPage(request, response, e.getMessage());
            }
        }
        String view = "/Instructor/MarkingViews/markingTableView.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
        // TODO: else:
        // ...
    }

    private void showErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/errorPage.jsp";
        ServletContext servletContext = getServletContext();
        request.setAttribute("errMsg", "Something wrong has happened. Please try again later.");
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    private void showErrorPage(HttpServletRequest request, HttpServletResponse response, String errMsg) throws ServletException, IOException {
        String view = "/errorPage.jsp";
        ServletContext servletContext = getServletContext();
        request.setAttribute("errMsg", errMsg);
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }
}
