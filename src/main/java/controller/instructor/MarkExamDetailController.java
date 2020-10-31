package main.java.controller.instructor;

import main.java.concurrency.AppSession;
import main.java.concurrency.AppSessionManager;
import main.java.concurrency.MarkingLockRemover;
import main.java.concurrency.MarkingLockManager;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/Instructor/submissions_detail")
public class MarkExamDetailController extends HttpServlet {

    public static final String APP_SESSION = "app_session";
    public static final String LOCK_REMOVER = "lock_remover";

    public MarkExamDetailController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println(request.getSession().getId());
        String examIDStr = request.getParameter("examID");
        String userIDStr = request.getParameter("userID");

        // Show error page if exam ID or user ID is not valid.
        if (examIDStr == null || userIDStr == null) {
            System.err.println("Error in MarkExamDetailedController doGet");
            showErrorPage(request, response);
            return;
        }
        int examID = 0;
        int userID = 0;
        try {
            examID = Integer.valueOf(examIDStr);
            userID = Integer.valueOf(userIDStr);
        } catch (Exception e) {
            System.err.println("Error in MarkExamDetailedController doGet");
            showErrorPage(request, response);
            return;
        }

        // Check app session
        MarkingLockManager lockManager = MarkingLockManager.getInstance();
        HttpSession httpSession = request.getSession(true);
        AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);
        if (appSession != null) {
            try {
                lockManager.releaseAllLocksByOwner(appSession.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        appSession = new AppSession(request.getRemoteUser(), httpSession.getId());
        AppSessionManager.setSession(appSession);
        httpSession.setAttribute(APP_SESSION, appSession);
        httpSession.setAttribute(LOCK_REMOVER, new MarkingLockRemover(appSession.getId()));

        // Acquire lock
        if (!lockManager.acquireSubmissionLock(examID, userID, request.getSession().getId())) {
            if (!lockManager.checkSubmissionLock(examID, userID, request.getSession().getId())) {
                // Show error page if we don't own the lock and cannot acquire the lock
                showErrorPage(request, response, "Someone else is accessing the page. Please try again later");
                return;
            }
        }

        // If lock is acquired, forward the page
        String view = "/Instructor/MarkingViews/markingDetailedView.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examID = Integer.valueOf(request.getParameter("examID"));
        int userID = Integer.valueOf(request.getParameter("userID"));

        // Check if we still have the lock
        MarkingLockManager lockManager = MarkingLockManager.getInstance();
        if (!lockManager.checkSubmissionLock(examID, userID, request.getSession().getId())) {
            showErrorPage(request, response, "This submission cannot be marked. Please refresh the page and try again later.");
        }

        // Update marks of submitted questions
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

        // Release lock
        try {
            HttpSession httpSession = request.getSession();
            AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);
            AppSessionManager.setSession(appSession);
            lockManager.releaseSubmissionLock(examID, userID, request.getSession().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Forward jsp
        if (updateSuccess) {
            String view = "/Instructor/MarkingViews/markingOverview.jsp";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
            requestDispatcher.forward(request, response);
        } else {
            System.out.println("Error in MarkExamDetailedController doPost: Update not successful");
            showErrorPage(request, response, "Update not successful.");
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
