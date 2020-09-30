package main.java.db.mapper;

import main.java.db.DBConnection;
import main.java.domain.Submission;
import main.java.domain.SubmittedQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

public class SubmissionMapper extends Mapper {

    /**
     * Get a submission of a student to an exam.
     * @param examID The exam_id of the exam.
     * @param userID The user_id of the student.
     * @return The Submission object.
     */
    public static Submission getSubmissionByIDs(int examID, int userID) {
        final String findSubmissionStmt =
                "SELECT * FROM submissions WHERE exam_id = ? AND user_id = ?";
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

    /**
     * Update fields of a submission when fudge points is changed.
     * @param examID The exam_id of the exam.
     * @param userID The user_id of the student.
     * @param fudgePoints The new fudge points.
     * @return True if update is successful.
     */
    public static boolean updateSubmission(int examID, int userID, float fudgePoints) {
        try {
            List<SubmittedQuestion> answers = SubmittedQuestionMapper.getSubmittedQuestions(examID, userID);
            boolean allQuestionsMarked = true;
            float totalMarks = 0;
            for (SubmittedQuestion answer : answers) {
                if (answer.isMarked()) {
                    totalMarks += answer.getMarks();
                } else { // If not all answers are marked
                    allQuestionsMarked = false;
                }
            }
            if (!allQuestionsMarked) {
                totalMarks = 0;
            } else {
                totalMarks += fudgePoints;
            }
            final String updateStmt =
                    "UPDATE submissions SET is_marked = ?, marks = ?, fudge_points = ? WHERE exam_id = ? AND user_id = ?";
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
            stmt.setBoolean(1, allQuestionsMarked);
            stmt.setFloat(2, totalMarks);
            stmt.setFloat(3, fudgePoints);
            stmt.setInt(4, examID);
            stmt.setInt(5, userID);
            int result = stmt.executeUpdate();
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}