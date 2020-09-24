package db.mapper;

import db.DBConnection;
import domain.Exam;
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
}
