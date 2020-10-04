package main.java.controller.instructor;

import main.java.db.mapper.ExamMapper;
import main.java.db.mapper.SubmissionMapper;
import main.java.db.mapper.SubmittedQuestionMapper;
import main.java.domain.Exam;
import main.java.domain.Question;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/submissions_detail")
public class MarkExamDetailController extends HttpServlet {

    public MarkExamDetailController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String examID = request.getParameter("examID");
        String userID = request.getParameter("userID");
        if (examID != null && userID != null) {
            String view = "/Instructor/MarkingViews/markingDetailedView.jsp";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
            requestDispatcher.forward(request, response);
        } else {
            System.out.println("Error in MarkExamDetailedController doGet");
            showErrorPage(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examID = Integer.valueOf(request.getParameter("examID"));
        int userID = Integer.valueOf(request.getParameter("userID"));
        Exam exam = ExamMapper.getExamByID(examID);
        List<Question> questions = exam.getQuestions();
        for (Question question : questions) {
            int questionNumber = question.getQuestionNumber();
            String marks = request.getParameter("marksQ"+questionNumber);
            try {
                // If there's marks and marks are legal, update the marks in the submitted_questions table
                float numericMarks = Float.valueOf(marks);
                SubmittedQuestionMapper.updateMarks(examID, userID, questionNumber, numericMarks);
            } catch (Exception e) {
                continue;
            }
        }

        // update submission according to marks and fudge points
        boolean updateSuccess;
        try {
            // If there's a valid fudge point, update it
            float fudgePoints = Float.valueOf(request.getParameter("fudgePoints"));
            updateSuccess = SubmissionMapper.updateSubmission(examID, userID, fudgePoints);
        } catch (Exception e) {
            // If there's no valid fudge point, update submission based on the submitted questions
            updateSuccess = SubmissionMapper.updateSubmission(examID, userID);
        }
        if (updateSuccess) {
            String view = "/Instructor/MarkingViews/markingOverview.jsp";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
            requestDispatcher.forward(request, response);
        } else {
            System.out.println("Error in MarkExamDetailedController doPost: Update not successful");
            showErrorPage(request, response, "Update not successful");
        }
    }

    private void showErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/errorPage.jsp";
        ServletContext servletContext = getServletContext();
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
