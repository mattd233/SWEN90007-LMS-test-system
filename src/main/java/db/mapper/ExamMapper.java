package main.java.db.mapper;

import main.java.db.DBConnection;
import main.java.domain.Exam;
import main.java.domain.Question;
import main.java.domain.Student;
import main.java.domain.Submission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamMapper extends Mapper {

    /**
     * A function that retrieves all exams related to the subject identified by subject code.
     * @param subjectCode identifies the subject that we are trying to retrieve exams from
     * @return an array list of exams of the given subject
     */
    public static List<Exam> getAllExamsWithSubjectCode(String subjectCode) {
        final String findAllExamsStmt =
                "SELECT * FROM exams WHERE subject_code = ? ORDER BY exam_id";

        List<Exam> exams = new ArrayList<>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findAllExamsStmt);
            stmt.setString(1, subjectCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int exam_id = rs.getInt(1);
                String title = rs.getString(3);
                String description = rs.getString(4);
                Exam.ExamStatus status = Exam.ExamStatus.valueOf(Exam.ExamStatus.class, rs.getString(5));
                exams.add(new Exam(exam_id, subjectCode, title, description, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    /**
     * Get the status of the exam.
     * @param examID id of the exam.
     * @return A string out of (PUBLISHED, CLOSED, UNPUBLISHED)
     */
    public static String getExamStatus(int examID){
        final String findExamStmt = "SELECT * FROM exams WHERE exam_id = ?";
        try{
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findExamStmt);
            stmt.setInt(1, examID);
            String status = "";
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exam.ExamStatus examStatus = Exam.ExamStatus.valueOf(Exam.ExamStatus.class, rs.getString(5));
                if(examStatus.toString().equals("PUBLISHED")){
                    status = "PUBLISHED";
                    return status;
                } else if (examStatus.toString().equals("CLOSED")){
                    status = "CLOSED";
                    return status;
                } else if (examStatus.toString().equals("UNPUBLISHED")){
                    status = "UNPUBLISHED";
                    return status;
                } else {
                    System.err.println("error when getting the exam status");
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a single exam by its id.
     * @param examID
     * @return An exam item given by examID.
     */
    public static Exam getExamByID(int examID) {
        final String findExamStmt = "SELECT * FROM exams WHERE exam_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findExamStmt);
            stmt.setInt(1, examID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String subjectCode = rs.getString(2);
                String title = rs.getString(3);
                String description = rs.getString(4);
                Exam.ExamStatus status = Exam.ExamStatus.valueOf(Exam.ExamStatus.class, rs.getString(5));
                return new Exam(examID, subjectCode, title, description, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert an exam into the database and get its id back.
     * @param exam exam object to be inserted
     * @return the exam_id of the newly inserted exam.
     */
    public static int insert(Exam exam) {

        final String insertExamStmt = "INSERT INTO exams VALUES (DEFAULT, ?, ?, ?, DEFAULT)";
        final String getIDStmt = "SELECT currval(pg_get_serial_sequence('exams','exam_id'))";

        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement insertStmt = dbConnection.prepareStatement(insertExamStmt);
            PreparedStatement getIDStatement = dbConnection.prepareStatement(getIDStmt);
            insertStmt.setString(1,exam.getSubjectCode());
            insertStmt.setString(2, exam.getTitle());
            insertStmt.setString(3, exam.getDescription());
            insertStmt.execute();
            ResultSet rs = getIDStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Update an exam object's title and description in the db
     * @param exam exam object to be update
     */
    public static void update(Exam exam) {

        final String updateExamStmt = "UPDATE exams SET title = ?, description = ? WHERE exam_id = ?";

        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateExamStmt);
            stmt.setString(1,exam.getTitle());
            stmt.setString(2, exam.getDescription());
            stmt.setInt(3, exam.getExamID());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Publish an exam. Exams can only be published if its current status is UNPUBLISHED.
     * @param examID
     * @return True if the exam is successfully published. Otherwise returns false.
     */
    public static boolean publishExam(int examID) {
        final String updateStmt =
                "UPDATE exams SET status = ?::exam_status WHERE exam_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
            stmt.setString(1, Exam.ExamStatus.PUBLISHED.toString());
            stmt.setInt(2, examID);
            int result = stmt.executeUpdate();
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Close an exam and create submissions for all students who haven't submitted.
     * Exams can only be closed if its current status is PUBLISHED.
     * @param examID
     * @return True if exam is closed successfully. Otherwise returns false.
     */
    public static boolean closeExam(int examID) {
        final String updateStmt =
                "UPDATE exams SET status = ?::exam_status WHERE exam_id = ?";
        try {
            // Set exam status to closed in db
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
            stmt.setString(1, Exam.ExamStatus.CLOSED.toString());
            stmt.setInt(2, examID);
            int result = stmt.executeUpdate();
            // Create submissions for all students
            Exam exam = ExamMapper.getExamByID(examID);
            List<Question> questions = exam.getQuestions();
            String subjectCode = exam.getSubjectCode();
            List<Student> students = UserSubjectMapper.getAllStudentsWithSubject(subjectCode);
            for (Student student : students) {
                int userID = student.getUserID();
                // If there's not submission from this student, create a submission
                if (SubmissionMapper.getSubmissionByIDs(examID, userID) == null) {
                    Submission submission = new Submission(examID, userID);
                    SubmissionMapper.insertSubmission(submission);
                    for (Question question : questions) {
                        SubmittedQuestionMapper.insertUnansweredSubmittedQuestion(question, userID);
                    }
                }
            }
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete an exam from the database.
     * Exams can only be deleted if no student is doing the exam or have submitted an submission.
     * @param examID
     * @return True if the exam is successfully deleted. Otherwise returns false.
     */
    public static boolean deleteExam(int examID) {
        // Don't need to delete exam if exam doesn't exist
        final String getExamStmt =
                "SELECT * FROM exams WHERE exam_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(getExamStmt);
            stmt.setInt(1, examID);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Exam doesn't exist.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Can't delete exam if there's submission
        final String getSubmissionStmt =
                "SELECT * FROM submissions WHERE exam_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(getSubmissionStmt);
            stmt.setInt(1, examID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Cannot delete exam because there are already submissions.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // TODO: can't delete exam if there's student taking exam

        // Delete exam
        final String updateStmt =
                "DELETE FROM exams WHERE exam_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
            stmt.setInt(1, examID);
            int result = stmt.executeUpdate();
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
