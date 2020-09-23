package db.mapper;

import db.DBConnection;
import domain.Exam;
import domain.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExamMapper extends Mapper {

    /**
     * A function that retrieves all exams related to the subject identified by subject code.
     * @param subjectCode identifies the subject that we are trying to retrieve exams from
     * @return an array list of exams of the given subject
     */
    public static List<Exam> getAllExamsWithSubjectCode(String subjectCode) {
        final String findAllExamsStmt =
                "SELECT * FROM exams WHERE subject_code = ?";

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


}
