package main.java.db.mapper;

import main.java.db.DBConnection;
import main.java.domain.SubmittedQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SubmittedQuestionMapper {

    /**
     * Get all submitted questions of an exam from a student
     * @param examID The exam_id of the exam.
     * @param userID The user_id of the student.
     * @return A list of SubmittedQuestion objects.
     */
    public static List<SubmittedQuestion> getSubmittedQuestions(int examID, int userID) {
        final String findAnswerStmt =
                "SELECT * FROM submitted_questions WHERE exam_id = ? AND user_id = ?";
        List<SubmittedQuestion> submittedQuestions = new ArrayList<SubmittedQuestion>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findAnswerStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int qNumber = rs.getInt(3);
                String qType = rs.getString(4);
                int cNumber = rs.getInt(5);
                String shortAnswer = rs.getString(6);
                boolean isMarked = rs.getBoolean(7);
                float marks = rs.getFloat(8);
                submittedQuestions.add(new SubmittedQuestion(examID, userID, qNumber, qType, cNumber, shortAnswer, isMarked, marks));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return submittedQuestions;
    }

    /**
     * Get a particular submitted question.
     * @param examID The exam_id of the exam.
     * @param userID The user_id of the student.
     * @param questionNumber The question_number of the submitted_question.
     * @return The SubmittedQuestion object.
     */
    public static SubmittedQuestion getSubmittedQuestion(int examID, int userID, int questionNumber) {
        final String findAnswerStmt =
                "SELECT * FROM submitted_questions WHERE exam_id = ? AND user_id = ? AND question_number = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findAnswerStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, userID);
            stmt.setInt(3, questionNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String qType = rs.getString(4);
                int cNumber = rs.getInt(5);
                String shortAnswer = rs.getString(6);
                boolean isMarked = rs.getBoolean(7);
                float marks = rs.getFloat(8);
                return new SubmittedQuestion(examID, userID, questionNumber, qType, cNumber, shortAnswer, isMarked, marks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update the marks of a submitted question and set is_marked to true.
     * @param examID The exam_id of the exam.
     * @param userID The user_id of the student.
     * @param questionNumber The question_number of the submitted_question.
     * @param marks The marks of the submitted_question.
     * @return True if update is successful.
     */
    public static boolean updateMarks(int examID, int userID, int questionNumber, float marks) {
        final String updateMarksStmt =
                "UPDATE submitted_questions SET is_marked = TRUE, marks = ? WHERE exam_id = ? AND user_id = ? AND question_number = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateMarksStmt);
            stmt.setFloat(1, marks);
            stmt.setInt(2, examID);
            stmt.setInt(3, userID);
            stmt.setInt(4, questionNumber);
            int result = stmt.executeUpdate();
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
