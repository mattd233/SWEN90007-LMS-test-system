package db.mapper;

import db.DBConnection;
import domain.MultipleChoiceQuestion;
import domain.Question;
import domain.ShortAnswerQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionMapper {

    /**
     * Get all questions of an exam.
     * @param examID the exam_id of the exam
     * @return a list of Question objects
     */
    public static List<Question> getAllQuestionsWithExamID(int examID) {
        final String findAllQuestionsStmt = "SELECT * FROM questions WHERE exam_id = ?";
        List<Question> questions = new ArrayList<>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findAllQuestionsStmt);
            stmt.setInt(1, examID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int qNumber = rs.getInt(2);
                String title = rs.getString(4);
                String description = rs.getString(5);
                int marks = rs.getInt(6);
                String qType = rs.getString(3);
                if (qType.equals(Question.QuestionType.MULTIPLE_CHOICE.toString())) {
                    MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(examID, qNumber, title, description, marks);
                    mcq.setChoices(ChoiceMapper.getChoices(examID, qNumber));
                    questions.add(mcq);
                } else if (qType.equals(Question.QuestionType.SHORT_ANSWER.toString())) {
                    questions.add(new ShortAnswerQuestion(examID, qNumber, title, description, marks));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    /**
     *
     * @param exam_id
     * @param question_number
     * @return
     */
    public static Question getQuestionWithQuestionID(int exam_id, int question_number) {
        final String findQuestionStmt = "SELECT * FROM questions WHERE exam_id = ? AND question_number = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findQuestionStmt);
            stmt.setInt(1, exam_id);
            stmt.setInt(2, question_number);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Question.QuestionType question_type = Question.QuestionType.valueOf(Question.QuestionType.class, rs.getString(3));
                String title = rs.getString(4);
                String description = rs.getString(5);
                int marks = rs.getInt(6);
                // return a question by telling the question type
                if (question_type == Question.QuestionType.MULTIPLE_CHOICE){
                    return new MultipleChoiceQuestion(exam_id, question_number, title, description, marks);
                }
                else if (question_type == Question.QuestionType.SHORT_ANSWER){
                    return new ShortAnswerQuestion(exam_id, question_number, title, description, marks);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insert(Question question) {
        final String insertQuestionStmt = "INSERT INTO questions VALUES (?, ?, ?::question_type, ?, ?, ?)";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(insertQuestionStmt);
            stmt.setInt(1, question.getExamID());
            stmt.setInt(2, question.getQuestionNumber());
            if (question instanceof MultipleChoiceQuestion) {
                stmt.setString(3, Question.QuestionType.MULTIPLE_CHOICE.toString());
            } else {
                stmt.setString(3, Question.QuestionType.SHORT_ANSWER.toString());
            }
            stmt.setString(4, question.getTitle());
            stmt.setString(5, question.getDescription());
            stmt.setInt(6, question.getMarks());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param exam_id
     * @param question_number
     * @return
     */
    public static Question getNextQuestion(int exam_id, int question_number){
        try{
            List<Question> questionList = getAllQuestionsWithExamID(exam_id);
            Question thisQuestion = getQuestionWithQuestionID(exam_id, question_number);
            int index;
            Question nextQuestion;

            // assert if the question is in the list
            if (questionList.contains(thisQuestion)){
                // the question is not the last question in the list
                index = questionList.indexOf(thisQuestion);
                nextQuestion = questionList.get(index + 1);
                return nextQuestion;
            }
            else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
