package main.java.controller.instructor;

import main.java.db.mapper.ChoiceMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.domain.Choice;
import main.java.domain.MultipleChoiceQuestion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Instructor/saveExam")
public class SaveExamController extends HttpServlet {

    public SaveExamController(){
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exam_id = request.getParameter("exam_id");
        System.out.println(exam_id);
        int examID = Integer.parseInt(exam_id);
//        List<Question> questions = new ArrayList<>();
        int questionIdx = 1;
        while(request.getParameter("title" + questionIdx) != null) {
            String type = request.getParameter("type" + questionIdx);
            String title = request.getParameter("title" + questionIdx);
            String description = request.getParameter("description" + questionIdx);
            int marks = Integer.parseInt(request.getParameter("marks" + questionIdx));
//            List<Choice> choices = new ArrayList<>();
            if (type.equals("multiple_choice")) {
                int choiceIdx = 1;
                while(request.getParameter("Q" + questionIdx + "choice" + choiceIdx)!=null) {
//                    choices.add(new Choice(examID,questionIdx,choiceIdx,request.getParameter("Q" + questionIdx + "choice" + choiceIdx)));
                    ChoiceMapper.insert(new Choice(examID,questionIdx, choiceIdx, request.getParameter("Q" + questionIdx + "choice" + choiceIdx)));
                    choiceIdx++;
                }
//                questions.add(new MultipleChoiceQuestion(examID, questionIdx, title, description, marks));
                QuestionMapper.insert(new MultipleChoiceQuestion(examID, questionIdx, title, description, marks));
            } else {
//                questions.add(new ShortAnswerQuestion(examID, questionIdx, title, description, marks));
                QuestionMapper.insert(new MultipleChoiceQuestion(examID, questionIdx, title, description, marks));
            }
            questionIdx++;
        }

    }
}
