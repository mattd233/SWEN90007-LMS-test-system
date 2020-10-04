package main.java.controller.instructor;

import main.java.db.UOW.ChoiceUOW;
import main.java.db.UOW.QuestionUOW;
import main.java.db.mapper.ExamMapper;
import main.java.domain.Choice;
import main.java.domain.Exam;
import main.java.domain.MultipleChoiceQuestion;
import main.java.domain.ShortAnswerQuestion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Instructor/createNewExam")
public class CreateExamController extends HttpServlet {

    public CreateExamController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // add the exam
        String subjectCode = request.getParameter("subject_code");
        String examTitle = request.getParameter("exam_title");
        String examDescription = request.getParameter("exam_description");
        Exam exam = new Exam(subjectCode, examTitle, examDescription);
        int examID = ExamMapper.insert(exam);
        assert examID != -1;

        QuestionUOW.newCurrent();
        ChoiceUOW.newCurrent();

        // add the exam questions
        int questionIdx = 1;
        while(request.getParameter("title" + questionIdx) != null) {
            String type = request.getParameter("type" + questionIdx);
            String title = request.getParameter("title" + questionIdx);
            String description = request.getParameter("description" + questionIdx);
            int marks = Integer.parseInt(request.getParameter("marks" + questionIdx));
            if (type.equals("multiple_choice")) {
                int choiceIdx = 1;
                while (request.getParameter("Q" + questionIdx + "choice" + choiceIdx) != null) {
                    ChoiceUOW.getCurrent().registerNew(new Choice(examID, questionIdx, choiceIdx, request.getParameter("Q" + questionIdx + "choice" + choiceIdx)));
                    choiceIdx++;
                }
                QuestionUOW.getCurrent().registerNew(new MultipleChoiceQuestion(examID, questionIdx, title, description, marks));
            } else {
                QuestionUOW.getCurrent().registerNew(new ShortAnswerQuestion(examID, questionIdx, title, description, marks));
            }
            questionIdx++;
        }
        QuestionUOW.getCurrent().commit();
        ChoiceUOW.getCurrent().commit();
        response.sendRedirect("/Instructor/instructorExams.jsp?subject_code=" + subjectCode);
    }

}
