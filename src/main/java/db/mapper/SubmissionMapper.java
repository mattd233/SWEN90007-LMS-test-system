package main.java.db.mapper;


import main.java.db.DBConnection;
import main.java.domain.Submission;
import main.java.domain.SubmittedQuestion;

import java.sql.*;
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
     * Recalculate total marks from all submitted questions and the old submission-level fudge points,
     * and update fields of a submission. This method should be called when updating submission mark
     * from the marking detailed view.
     * @param examID The exam_id of the exam.
     * @param userID The user_id of the student.
     * @return True if update is successful.
     */
    public static boolean updateSubmission(int examID, int userID) {
        try {
            Submission submission = getSubmissionByIDs(examID, userID);
            float fudgePoints = submission.getFudgePoints();
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
                totalMarks = -1;
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
            UserSubjectMapper.updateMarks(userID, ExamMapper.getExamByID(examID).getSubjectCode());
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recalculate total marks from all submitted questions and the new submission-level fudge points,
     * and update fields of a submission. This method should be called when updating submission mark
     * from the marking detailed view.
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
                totalMarks = -1;
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
            UserSubjectMapper.updateMarks(userID, ExamMapper.getExamByID(examID).getSubjectCode());
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update fields of a submission when total marks is changed. This method should be called when
     * updating submission mark from the marking table view.
     * @param examID The exam_id of the exam.
     * @param userID The user_id of the student.
     * @param marks The new marks.
     * @return True if update is successful.
     */
    public static boolean updateSubmissionMarks(int examID, int userID, float marks) {
        try {
            final String updateStmt =
                    "UPDATE submissions SET marks = ? WHERE exam_id = ? AND user_id = ?";
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
            stmt.setFloat(1, marks);
            stmt.setInt(2, examID);
            stmt.setInt(3, userID);
            int result = stmt.executeUpdate();
            UserSubjectMapper.updateMarks(userID, ExamMapper.getExamByID(examID).getSubjectCode());
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check whether a submission exists.
     * @param exam_id
     * @param student_id
     * @return
     */
    public static boolean checkSubmission(int exam_id, int student_id){
        final String findStudentSubmissionsStmt =
                "SELECT * from submissions WHERE exam_id = ? AND user_id = ? ";
        boolean flag = false;
        try{
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findStudentSubmissionsStmt);
            stmt.setInt(1, exam_id);
            stmt.setInt(2, student_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

     /**
      * Insert a submission
      * @param submission
      */
    public static void insertSubmission(Submission submission) {

        final String insertSStmt =
                "INSERT INTO submissions VALUES (?, ?, ?, DEFAULT, DEFAULT, DEFAULT)";

        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement insertStmt = dbConnection.prepareStatement(insertSStmt);
            insertStmt.setInt(1, submission.getExamID());
            insertStmt.setInt(2, submission.getUserID());
            insertStmt.setTimestamp(3, submission.getSubmissionTime());
            insertStmt.execute();
            System.out.println("insert submission successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
