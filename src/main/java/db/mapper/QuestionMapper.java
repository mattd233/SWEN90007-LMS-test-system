package main.java.db.mapper;


import main.java.db.DBConnection;
import main.java.domain.MultipleChoiceQuestion;
import main.java.domain.Question;
import main.java.domain.ShortAnswerQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionMapper extends Mapper {

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
     * Insert a question in to the database.
     * @param question question to be inserted.
     */
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
     * Update a question in the database.
     * @param question question to be updated.
     */
    public static void update(Question question) {
        final String updateQuestionStmt = "UPDATE questions SET question_type = ?::question_type, " +
                "title = ?, description = ?, marks = ?" +
                "WHERE exam_id = ? AND question_number = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateQuestionStmt);
            if (question instanceof MultipleChoiceQuestion) {
                stmt.setString(1, Question.QuestionType.MULTIPLE_CHOICE.toString());
            } else {
                stmt.setString(1, Question.QuestionType.SHORT_ANSWER.toString());
            }
            stmt.setString(2, question.getTitle());
            stmt.setString(3, question.getDescription());
            stmt.setInt(4, question.getMarks());
            stmt.setInt(5, question.getExamID());
            stmt.setInt(6, question.getQuestionNumber());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete a question in the database.
     * @param question question to be deleted.
     */
    public static void delete(Question question) {
        final String deleteQuestionStmt = "DELETE FROM questions WHERE exam_id = ? AND question_number = ? ";
        final String deleteChoicesStmt = "DELETE FROM choices WHERE exam_id = ? AND question_number = ? ";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(deleteQuestionStmt);
            stmt.setInt(1, question.getExamID());
            stmt.setInt(2, question.getQuestionNumber());
            stmt.execute();
            // take care of choices table if it's a multiple choice question
            if (question instanceof MultipleChoiceQuestion) {
                stmt = dbConnection.prepareStatement(deleteChoicesStmt);
                stmt.setInt(1, question.getExamID());
                stmt.setInt(2, question.getQuestionNumber());
                stmt.execute();
            }
            // reset the question numbers
//            resetQuestionNumber(question.getExamID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the biggest question number of given exam id.
     * @param examID id of the exam
     */
    public static int getCurQuestionNumber(int examID) {
        final String getQNumberStmt = "SELECT max(question_number) FROM questions WHERE exam_id = ?";

        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt;
            stmt = dbConnection.prepareStatement(getQNumberStmt);
            stmt.setInt(1, examID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // no question in this exam, return 0
        return 0;

    }

}
