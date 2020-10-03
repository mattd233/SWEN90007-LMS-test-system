package main.java.controller.instructor;

import main.java.db.ChoiceUOW;
import main.java.db.QuestionUOW;
import main.java.db.mapper.ChoiceMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.domain.Choice;
import main.java.domain.Question;
import main.java.domain.ShortAnswerQuestion;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Instructor/editExam")
public class EditExamController extends HttpServlet {

    public EditExamController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int exam_id = Integer.parseInt(request.getParameter("exam_id"));
        String view = "/Instructor/editExam.jsp?exam_id=" + exam_id;
        if (request.getParameter("deleteQuestion")!=null) {
            int questionNumber = Integer.parseInt(request.getParameter("deleteQuestion"));
            QuestionMapper.delete(new ShortAnswerQuestion(exam_id, questionNumber, "", "", 0));
        }
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        QuestionUOW.newCurrent();
        ChoiceUOW.newCurrent();

        String exam_id = request.getParameter("exam_id");
        int examID = Integer.parseInt(exam_id);
        int questionIdx = 1;
        List<Question> questions = QuestionMapper.getAllQuestionsWithExamID(examID);
        while(request.getParameter("title" + questionIdx) != null) {
            Question question = questions.get(questionIdx-1);
            boolean modified = false;

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
                List<Choice> choices = ChoiceMapper.getChoices(examID, questionIdx);
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
        QuestionUOW.getCurrent().commit();
        ChoiceUOW.getCurrent().commit();
        response.sendRedirect("/Instructor/editExam.jsp?exam_id=" + examID);
    }

}
