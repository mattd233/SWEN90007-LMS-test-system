package controller;

import db.mapper.QuestionMapper;
import db.mapper.SubmissionMapper;
import db.mapper.SubmittedQuestionMapper;
import domain.Question;

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
        String subjectCode = request.getParameter("subject");
        String examID = request.getParameter("examID");
        String userID = request.getParameter("userID");
        String view = "/errorPage.jsp";
        if (subjectCode != null) {
            view = "/Instructor/MarkingViews/markingTableView.jsp";
        } else if (examID != null && userID != null) {
            view = "/Instructor/MarkingViews/markingDetailedView.jsp";
        } else {
            System.out.println("Error in MarkExamController doGet");
        }
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
        return;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examID = Integer.valueOf(request.getParameter("examID"));
        int userID = Integer.valueOf(request.getParameter("userID"));
        List<Question> questions = QuestionMapper.getAllQuestionsWithExamID(examID);
        for (Question question : questions) {
            int questionNumber = question.getQuestionNumber();
            String marks = request.getParameter("marksQ"+questionNumber);
            try {
                float numericMarks = Float.valueOf(marks);
                SubmittedQuestionMapper.updateMarks(examID, userID, questionNumber, numericMarks); // TODO: abort all changes if one update fails?
            } catch (Exception e) {
                e.printStackTrace();
                showErrorPage(request, response);
                return;
            }
        }
        float fudgePoints = Float.valueOf(request.getParameter("fudgePoints"));
        boolean updateSuccess = SubmissionMapper.updateSubmission(examID, userID, fudgePoints);
        if (updateSuccess) {
            String view = "/Instructor/MarkingViews/markingOverview.jsp";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
            requestDispatcher.forward(request, response);
        } else {
            System.out.println("Error in MarkExamController doPost");
            showErrorPage(request, response);
            return;
        }
    }

    private void showErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/errorPage.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }
}
