package main.java.controller.instructor;

import main.java.concurrency.AppSession;
import main.java.concurrency.AppSessionManager;
import main.java.concurrency.MarkingLockManager;
import main.java.concurrency.MarkingLockRemover;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/Instructor/submissions_table")
public class MarkExamTableController extends HttpServlet {

    public static final String APP_SESSION = "app_session";
    public static final String LOCK_REMOVER = "lock_remover";

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
            System.err.println("Error in MarkExamTableController doGet");
            String msg = "Something wrong has happened. Please try again later.";
            response.getWriter().println(msg);
            return;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // lock manager
        MarkingLockManager lockManager = MarkingLockManager.getInstance();

        // Session details
        HttpSession httpSession = request.getSession(true);
        AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);
        if (appSession != null) {
            try {
                lockManager.releaseAllLocksByOwner(appSession.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        appSession = new AppSession(request.getRemoteUser(),
                httpSession.getId());
        AppSessionManager.setSession(appSession);
        httpSession.setAttribute(APP_SESSION, appSession);
        httpSession.setAttribute(LOCK_REMOVER, new MarkingLockRemover(appSession.getId()));

        // Update marks
        String subjectCode = request.getParameter("subject_code");
        List<Exam> exams = ExamMapper.getAllExamsWithSubjectCode(subjectCode);
        List<Student> students = UserSubjectMapper.getAllStudentsWithSubject(subjectCode);
        for (Student student : students) {
            int sID = student.getStudentID();
            // Check if version number is still up-to-date
            int version = Integer.valueOf(request.getParameter("v" + sID));
            int currVersion = UserSubjectMapper.getVersion(sID, subjectCode);
            if (version != currVersion) { // If version is expired, cannot update the student's marks
                String msg = "Some of the marks cannot be updated because the data has expired. Please refresh the page and try again.";
                response.getWriter().println(msg);
                return;
            } else { // If version is up to date, update the submission
                try {
                    // Try to acquire locks for all submissions of this student of this subject
                    if (lockManager.acquireAllLocksOfStudentSubject(sID, subjectCode, request.getSession().getId())) {
                        // If lock can be acquired, update submissions
                        for (Exam exam : exams) {
                            int eID = exam.getExamID();
                            String marksStr = request.getParameter("m_"+eID+"_"+sID);
                            if (marksStr != null && !marksStr.equals("")) {
                                float marks = Float.valueOf(marksStr);
                                SubmissionMapper.updateSubmissionMarks(eID, sID, marks);
                            }
                        }
                        // Release all locks
                        lockManager.releaseAllLocksByOwner(request.getSession().getId());
                    } else {
                        // If lock cannot be acquired, mark cannot be updated
                        String msg = "Some of the marks cannot be updated because the data has expired. Please refresh the page and try again.";
                        response.getWriter().println(msg);
                        return;
                    }
                } catch (Exception e) {
                    // In case of any exceptions, mark cannot be updated
                    e.printStackTrace();
                    String msg = "Update cannot be done.";
                    response.getWriter().println(msg);
                    return;
                }

                // Update users_has_subjects table
                String fudgePointsStr = request.getParameter("fp"+sID);
                try {
                    float fudgePoints = Float.valueOf(fudgePointsStr);
                    boolean updateSuccess = UserSubjectMapper.updateFudgePoints(sID, subjectCode, fudgePoints);
                    if (!updateSuccess) {
                        String msg = "Update unsuccessful.";
                        response.getWriter().println(msg);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String view = "/Instructor/MarkingViews/markingTableView.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }
}
