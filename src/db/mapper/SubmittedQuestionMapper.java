package db.mapper;

import db.DBConnection;
import domain.SubmittedQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * Insert a submitted question into the database
     * @param submittedQuestion
     * @return
     *     exam_id SERIAL REFERENCES exams(exam_id),
     *     user_id INT REFERENCES users(user_id),
     *     question_number SMALLINT NOT NULL,
     *     question_type question_type NOT NULL,
     *     choice_number SMALLINT DEFAULT null,
     *     short_answer VARCHAR(500) DEFAULT null,
     *     is_marked BOOLEAN NOT NULL DEFAULT FALSE,
     *     marks FLOAT DEFAULT null,
    **/
    public static int insertSQ(SubmittedQuestion submittedQuestion) {

        final String insertSBStmt = "INSERT INTO submitted_questions VALUES (?, ?, ?, ?, ?, ?, DEFAULT, DEFAULT)";

        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement insertStmt = dbConnection.prepareStatement(insertSBStmt);

            insertStmt.setInt(1, submittedQuestion.getExamID());
            insertStmt.setInt(2, submittedQuestion.getUserID());
            insertStmt.setInt(3, submittedQuestion.getQuestionNumber());
            insertStmt.setString(4, submittedQuestion.getQuestionType());
            if (submittedQuestion.getQuestionType().equals("SHORT_ANSWER")){
                insertStmt.setString(6, submittedQuestion.getShortAnswer());
                insertStmt.setString(5, null);
            }
            else if (submittedQuestion.getQuestionType().equals("MULTIPLE_CHOICE")){
                insertStmt.setInt(5,submittedQuestion.getChoiceNumber());
                insertStmt.setString(6,null);
            }
            insertStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
