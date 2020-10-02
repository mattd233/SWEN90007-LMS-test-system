package main.java.controller.instructor;

import main.java.db.ChoiceUOW;
import main.java.db.QuestionUOW;
import main.java.db.mapper.ExamMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.domain.Choice;
import main.java.domain.Exam;
import main.java.domain.MultipleChoiceQuestion;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Instructor/createNewExam")
public class AddExamController extends HttpServlet {

    public AddExamController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/Instructor/createNewExam.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // add the exam
        String subjectCode = request.getParameter("code");
        String examTitle = request.getParameter("exam_title");
        String examDescription = request.getParameter("exam_description");
        Exam exam = new Exam(subjectCode, examTitle, examDescription);
        int examID = ExamMapper.insert(exam);
        assert examID != -1;

        if (QuestionUOW.getCurrent()==null) {
            QuestionUOW.newCurrent();
        }
        if (ChoiceUOW.getCurrent()==null) {
            ChoiceUOW.newCurrent();
        }

        // add the exam questions
        int questionIdx = 1;
        while(request.getParameter("title" + questionIdx) != null) {
            String type = request.getParameter("type" + questionIdx);
            String title = request.getParameter("title" + questionIdx);
            String description = request.getParameter("description" + questionIdx);
            int marks = Integer.parseInt(request.getParameter("marks" + questionIdx));
            if (type.equals("multiple_choice")) {
                // get back the question ID
                int questionID = QuestionMapper.insert(new MultipleChoiceQuestion(examID, title, description, marks));
                assert questionID != -1;
                int choiceIdx = 1;
                while(request.getParameter("Q" + questionIdx + "choice" + choiceIdx)!=null) {
                    new Choice(questionIdx)
                    choiceIdx++;
                }
//                questions.add(new MultipleChoiceQuestion(examID, questionIdx, title, description, marks));

            } else {
                QuestionMapper.insert(new MultipleChoiceQuestion(examID, title, description, marks));
            }
            questionIdx++;
        }

    }

}
