package main.java.controller.student;

import main.java.db.mapper.ExamMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.db.mapper.SubmissionMapper;
import main.java.db.mapper.SubmittedQuestionMapper;
import main.java.domain.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 */
@WebServlet("/Student/studentSubmitExams")
public class SubmitExamController extends HttpServlet {
    public SubmitExamController(){
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentID = request.getParameter("student_id");
        int student_id = Integer.parseInt(studentID);
        String examID = request.getParameter("exam_id");
        int exam_id = Integer.parseInt(examID);
        List<Question> questions = QuestionMapper.getAllQuestionsWithExamID(exam_id);
        int length = questions.size();
        String[] keys = new String[length];
        String[] answers = new String[length];
        HttpSession session = request.getSession();
        String status = ExamMapper.getExamStatus(exam_id);
        assert status != null;
        int question_index = Integer.parseInt(request.getParameter("index"));
        String question_answer = request.getParameter("answer");
        String key = exam_id + "_" + question_index;
        session.setAttribute(key,question_answer);
        System.out.println(key + "_"+ question_answer);
        if (status.equals("PUBLISHED")) {
            // get all the answers using session
            // int examID, int userID, int questionNumber, String questionType, int choiceNumber, String shortAnswer, boolean isMarked, float marks
            SubmittedQuestion sq;
            //choice_number
            for (int index = 0; index < length; index++) {
                keys[index] = exam_id + "_" + index;
                answers[index] = (String) session.getAttribute(keys[index]);
                int question_number = questions.get(index).getQuestionID();
                String answer = answers[index];
                System.out.println(Arrays.toString(keys) +"_"+ Arrays.toString(answers));
                try {
                    if (questions.get(index).getClass().equals(ShortAnswerQuestion.class)) {
                        sq = new SubmittedQuestion(exam_id, student_id, question_number, "SHORT_ANSWER", 0, answer, false, -1);
                        SubmittedQuestionMapper.insertSubmittedQuestion(sq);
                        System.out.println("insert submitted question " + index + " successfully.");
                    } else if (questions.get(index).getClass().equals(MultipleChoiceQuestion.class)) {
                        sq = new SubmittedQuestion(exam_id, student_id, question_number, "MULTIPLE_CHOICE", Integer.parseInt(answer) + 1, null, false, -1);
                        SubmittedQuestionMapper.insertSubmittedQuestion(sq);
                        System.out.println("insert submitted question " + index + " successfully.");
                    } else {
                        System.out.println("insert submitted question " + index + "  error.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

             //submission
            try{
                Submission submission = new Submission(exam_id, student_id);
                SubmissionMapper.insertSubmission(submission);
                System.out.println("insert submission successfully.");
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (status.equals("CLOSED")){
            for (Question question : questions) {
                SubmittedQuestionMapper.insertUnansweredSubmittedQuestion(question, student_id);
            }
            try{
                Submission submission = new Submission(exam_id, student_id);
                SubmissionMapper.insertSubmission(submission);
                System.out.println("insert submission as unanswered successfully.");
            } catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Unsuccessfully. The exam has been closed.");
        }
    }
}
