package main.java.controller.instructor;

import main.java.concurrency.*;
import main.java.db.UOW.ChoiceUOW;
import main.java.db.UOW.QuestionUOW;
import main.java.db.mapper.ExamMapper;
import main.java.db.mapper.LockMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.domain.*;

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

@WebServlet("/Instructor/editExam")
public class EditExamController extends HttpServlet {

    public static final String APP_SESSION = "app_session";
    public static final String LOCK_REMOVER = "lock_remover";


    public EditExamController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examID = Integer.parseInt(request.getParameter("exam_id"));
        ExclusiveReadLockManager lockManager = ExclusiveReadLockManager.getInstance();
        HttpSession httpSession = request.getSession(true);
        AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);

        if (appSession  != null) {
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
        httpSession.setAttribute(LOCK_REMOVER, new ExclusiveReadLockRemover(appSession.getId()));

        // first try to acquire the lock
        if (!lockManager.acquireLock(examID, request.getSession().getId())) {
            // if failed, check if the lock is owned by the current thread
            if (!lockManager.checkLock(examID, request.getSession().getId())) {
                //if not, abort
                String msg = "Someone else is accessing the page.";
                response.getWriter().println(msg);
                return;
            }
        }

        // handle deletion of question
        if (request.getParameter("delete_question")!=null) {
            if (QuestionMapper.getAllQuestionsWithExamID(examID).size()<=1) {
                // cannot delete the last question
                String msg = "Cannot delete the last question.";
                response.getWriter().println(msg);
                return;
            } else {
                int questionNumber = Integer.parseInt(request.getParameter("delete_question"));
                QuestionMapper.delete(new ShortAnswerQuestion(examID, questionNumber, "", "", 0));
            }
        }

        // handles navigating back to upper directory
        if (request.getParameter("release_lock")!=null) {
            System.out.println("Trying to release lock");
            try {
                ExclusiveReadLockManager.getInstance().releaseLock(examID, request.getSession().getId());
                Exam exam = ExamMapper.getExamByID(examID);
                assert exam != null;
                response.sendRedirect("instructorExams.jsp?subject_code=" + exam.getSubjectCode());
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(request.getParameter("release_lock"));

        response.sendRedirect("/Instructor/editExam.jsp?exam_id=" + examID);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examID = Integer.parseInt(request.getParameter("exam_id"));
        HttpSession httpSession = request.getSession();
        AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);
        AppSessionManager.setSession(appSession);
        // check if the lock is still hold by the right owner
        if (!ExclusiveReadLockManager.getInstance().checkLock(examID, request.getSession().getId())){
            response.getWriter().println("Some error has occurred, please try again later.");
            return;
        }

        Exam exam = ExamMapper.getExamByID(examID);
        assert exam != null;

        String examTitle = request.getParameter("exam_title");
        String examDescription = request.getParameter("exam_description");
        if (!examTitle.equals("")) {
            exam.setTitle(examTitle);
        }
        if (!examDescription.equals("")) {
            exam.setDescription(examDescription);
        }
        ExamMapper.update(exam);

        QuestionUOW.newCurrent();
        ChoiceUOW.newCurrent();

        int questionIdx = 1;
        List<Question> questions = exam.getQuestions();
        while(request.getParameter("title" + questionIdx) != null) {
            Question question = questions.get(questionIdx-1);

            String type = request.getParameter("type" + questionIdx);
            String title = request.getParameter("title" + questionIdx);
            String description = request.getParameter("description" + questionIdx);
            String marks = request.getParameter("marks" + questionIdx);

            // only change if there is input in one of the text boxes
            if (!title.equals("")) {
                question.setTitle(title);
                QuestionUOW.getCurrent().registerDirty(question);
            }
            if (!description.equals("")) {
                question.setDescription(description);
                QuestionUOW.getCurrent().registerDirty(question);
            }
            if (!marks.equals("")) {
                question.setMarks(Integer.parseInt(marks));
                QuestionUOW.getCurrent().registerDirty(question);
            }

            if (type.equals("multiple_choice")) {
                List<Choice> choices = ((MultipleChoiceQuestion)question).getChoices();
                int choiceIdx = 1;
                while(request.getParameter("Q" + questionIdx + "choice" + choiceIdx)!=null) {
                    Choice choice = choices.get(choiceIdx-1);
                    String choiceDescription = request.getParameter("Q" + questionIdx + "choice" + choiceIdx);
                    if (!choiceDescription.equals("")) {
                        choice.setChoiceDescription(choiceDescription);
                        ChoiceUOW.getCurrent().registerDirty(choice);
                    }
                    choiceIdx++;
                }

            }
            questionIdx++;

        }

        // save the newly added questions questions
        questionIdx = 1;
        int dbIdx = QuestionMapper.getCurQuestionNumber(examID);
        while(request.getParameter("new_title" + questionIdx) != null) {
            String type = request.getParameter("new_type" + questionIdx);
            String title = request.getParameter("new_title" + questionIdx);
            String description = request.getParameter("new_description" + questionIdx);
            int marks = Integer.parseInt(request.getParameter("new_marks" + questionIdx));
            if (type.equals("multiple_choice")) {
                int choiceIdx = 1;
                while (request.getParameter("new_Q" + questionIdx + "choice" + choiceIdx) != null) {
                    ChoiceUOW.getCurrent().registerNew(new Choice(examID, dbIdx + 1, choiceIdx, request.getParameter("new_Q" + questionIdx + "choice" + choiceIdx)));
                    choiceIdx++;
                }
                QuestionUOW.getCurrent().registerNew(new MultipleChoiceQuestion(examID, dbIdx + 1, title, description, marks));
            } else {
                QuestionUOW.getCurrent().registerNew(new ShortAnswerQuestion(examID, dbIdx + 1, title, description, marks));
            }
            questionIdx++;
        }
        // commit the UOW
        QuestionUOW.getCurrent().commit();
        ChoiceUOW.getCurrent().commit();
        // release the lock before exiting
        try {
            ExclusiveReadLockManager.getInstance().releaseLock(examID, request.getSession().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("instructorExams.jsp?subject_code=" + exam.getSubjectCode());
    }
}
