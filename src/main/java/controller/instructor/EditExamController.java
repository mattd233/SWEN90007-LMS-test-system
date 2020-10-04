package main.java.controller.instructor;

import main.java.db.UOW.ChoiceUOW;
import main.java.db.UOW.QuestionUOW;
import main.java.db.mapper.ExamMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.domain.*;

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
        // handles deleting questions
        int exam_id = Integer.parseInt(request.getParameter("exam_id"));
        if (request.getParameter("deleteQuestion")!=null) {
            int questionNumber = Integer.parseInt(request.getParameter("deleteQuestion"));
            QuestionMapper.delete(new ShortAnswerQuestion(exam_id, questionNumber, "", "", 0));
        }
        response.sendRedirect("/Instructor/editExam.jsp?exam_id=" + exam_id);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String exam_id = request.getParameter("exam_id");
        int examID = Integer.parseInt(exam_id);
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
        int curIdx = QuestionMapper.getCurQuestionNumber(examID);
        while(request.getParameter("new_title" + questionIdx) != null) {
            String type = request.getParameter("new_type" + questionIdx);
            String title = request.getParameter("new_title" + questionIdx);
            String description = request.getParameter("new_description" + questionIdx);
            int marks = Integer.parseInt(request.getParameter("new_marks" + questionIdx));
            if (type.equals("multiple_choice")) {
                int choiceIdx = 1;
                while (request.getParameter("Q" + questionIdx + "choice" + choiceIdx) != null) {
                    ChoiceUOW.getCurrent().registerNew(new Choice(examID, questionIdx, choiceIdx, request.getParameter("new_Q" + questionIdx + curIdx + "choice" + choiceIdx)));
                    choiceIdx++;
                }
                QuestionUOW.getCurrent().registerNew(new MultipleChoiceQuestion(examID, questionIdx + curIdx, title, description, marks));
            } else {
                QuestionUOW.getCurrent().registerNew(new ShortAnswerQuestion(examID, questionIdx + curIdx, title, description, marks));
            }
            questionIdx++;
        }
        QuestionUOW.getCurrent().commit();
        ChoiceUOW.getCurrent().commit();

        response.sendRedirect("/Instructor/editExam.jsp?exam_id=" + examID);
    }

}
