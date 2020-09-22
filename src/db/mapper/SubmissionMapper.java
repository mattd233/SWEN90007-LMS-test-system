package db.mapper;

import db.DBConnection;
import domain.Submission;
import domain.SubmittedQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SubmissionMapper extends Mapper {

//    public static List<Submission> getAllSubmissions() {
//        final String findSubmissionStmt = "SELECT * FROM submissions";
//        List<Submission> submissions = new ArrayList<Submission>();
//        try {
//            Connection dbConnection = new DBConnection().connect();
//            PreparedStatement stmt = dbConnection.prepareStatement(findSubmissionStmt);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                int eId = rs.getInt(1);
//                int uId = rs.getInt(2);
//                Timestamp submissionTime = rs.getTimestamp(3);
//                Boolean isMarked = rs.getBoolean(4);
//                float marks = rs.getFloat(5);
//                submissions.add(new Submission(eId, uId, submissionTime, isMarked, marks));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return submissions;
//    }

    public static Submission getSubmissionByIDs(int examID, int userID) {
        final String findSubmissionStmt = "SELECT * FROM submissions WHERE exam_id = ? AND user_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findSubmissionStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int eId = rs.getInt(1);
                int uId = rs.getInt(2);
                Timestamp submissionTime = rs.getTimestamp(3);
                Boolean isMarked = rs.getBoolean(4);
                float marks = rs.getFloat(5);
                float fudgePoints = rs.getFloat(6);
                return new Submission(eId, uId, submissionTime, isMarked, marks, fudgePoints);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static List<SubmittedQuestion> getAllSubmittedQuestions(int examID, int userID) {
//        final String findAnswerStmt = "SELECT * FROM submitted_questions WHERE exam_id = ? AND user_id = ?";
//        List<SubmittedQuestion> answers = new ArrayList<SubmittedQuestion>();
//        try {
//            Connection dbConnection = new DBConnection().connect();
//            PreparedStatement stmt = dbConnection.prepareStatement(findAnswerStmt);
//            stmt.setInt(1, examID);
//            stmt.setInt(2, userID);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                int eId = rs.getInt(1);
//                int uId = rs.getInt(2);
//                int qNumber = rs.getInt(3);
//                String qType = rs.getString(4);
//                int cNumber = rs.getInt(5);
//                String shortAnswer = rs.getString(6);
//                boolean isMarked = rs.getBoolean(7);
//                float marks = rs.getFloat(8);
//                answers.add(new SubmittedQuestion(eId, uId, qNumber, qType, cNumber, shortAnswer, isMarked, marks));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return answers;
//    }
    public static SubmittedQuestion getSubmittedQuestion(int examID, int userID, int questionNumber) {
        final String findAnswerStmt = "SELECT * FROM submitted_questions WHERE exam_id = ? AND user_id = ? AND question_number = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findAnswerStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, userID);
            stmt.setInt(3, questionNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
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
}
