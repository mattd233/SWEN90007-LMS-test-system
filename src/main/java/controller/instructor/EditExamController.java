package main.java.controller.instructor;

import db.QuestionUOW;
import main.java.db.mapper.QuestionMapper;
import main.java.domain.Choice;
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

@WebServlet("/Instructor/editExam")
public class EditExamController extends HttpServlet {

    public EditExamController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/Instructor/editExam.jsp?exam_id=" + request.getParameter("exam_id");
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (QuestionUOW.getCurrent() == null) {
            QuestionUOW.newCurrent();
        }
        String exam_id = request.getParameter("exam_id");
        System.out.println(exam_id);
        int examID = Integer.parseInt(exam_id);
        List<Question> questions = QuestionMapper.getAllQuestionsWithExamID(examID);
        int questionIdx = 1;
        while(request.getParameter("title" + questionIdx) != null) {
//            int questionNumber =
            String type = request.getParameter("type" + questionIdx);
            String title = request.getParameter("title" + questionIdx);
            String description = request.getParameter("description" + questionIdx);
            String marks = request.getParameter("marks" + questionIdx);
//            int marks = Integer.parseInt();
            // only change if there is input in the textbook
            if (!title.equals("")) {
                questions.get(questionIdx).setTitle(title);
            }
            if (!description.equals("")) {
                questions.get(questionIdx).setTitle(description);
            }
            if (!marks.equals("")) {
                questions.get(questionIdx).setMarks(Integer.parseInt(marks));
            }
            System.out.println(marks);

//            List<Choice> choices = new ArrayList<>();
            if (type.equals("multiple_choice")) {
                System.out.println("True");
                int choiceIdx = 1;
                while(request.getParameter("Q" + questionIdx + "choice" + choiceIdx)!=null) {
//                    Choice choice = (new Choice(examID,questionIdx,choiceIdx,request.getParameter("Q" + questionIdx + "choice" + choiceIdx)));
//                    ChoiceMapper.insert(new Choice(examID,questionIdx, choiceIdx, request.getParameter("Q" + questionIdx + "choice" + choiceIdx)));
//                    System.out.println(questionIdx + "choice" + choice.getChoiceID() + ", " + choice.getChoiceDescription());
                    choiceIdx++;
                }

            }
            questionIdx++;
        }
//        QuestionUOW.getCurrent().commit();
    }

}
