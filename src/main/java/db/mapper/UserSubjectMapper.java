package main.java.db.mapper;

import main.java.db.DBConnection;
import main.java.domain.Exam;
import main.java.domain.Student;
import main.java.domain.StudentSubjectMark;
import main.java.domain.Submission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserSubjectMapper extends Mapper {

    /**
     * Get all students who are taking a subject.
     * @param subjectCode The subject code.
     * @return A list of Student objects.
     */
    public static List<Student> getAllStudentsWithSubject(String subjectCode) {
        final String getStudentsStmt =
                "SELECT * FROM users_has_subjects WHERE subject_code = ? ORDER BY user_id";
        List<Student> students = new ArrayList<Student>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(getStudentsStmt);
            stmt.setString(1, subjectCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int sID = rs.getInt(1);
                Student student = UserMapper.findStudentWithID(sID);
                if (student != null) students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Get all students who are taking a subject as a string. Student names are separated with commas.
     * @param subjectCode
     * @return
     */
    public static String getAllStudentsWithSubjectAsString(String subjectCode) {
        List<Student> students = getAllStudentsWithSubject(subjectCode);
        if (students.size() == 0) {
            return "No students enrolled in this subject";
        }
        String str = "";
        for (int i=0; i<students.size(); i++) {
            str += students.get(i).getName();
            if (i != students.size() - 1) {
                str += ", ";
            }
        }
        return str;
    }

    public static StudentSubjectMark getStudentSubjectMark(int userID, String subjectCode) {
        final String getStmt =
                "SELECT * FROM users_has_subjects WHERE user_id = ? AND subject_code = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(getStmt);
            stmt.setInt(1, userID);
            stmt.setString(2, subjectCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                float fudgePoints = rs.getFloat(3);
                float marks = rs.getFloat(4);
                int version = rs.getInt(5);
                return new StudentSubjectMark(userID, subjectCode, fudgePoints, marks, version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("UserSubjectMapper: couldn't get StudentSubjectMark of "+userID+", "+subjectCode);
        return null;
    }

    public static int getVersion(int userID, String subjectCode) {
        final String getStmt =
                "SELECT * FROM users_has_subjects WHERE user_id = ? AND subject_code = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(getStmt);
            stmt.setInt(1, userID);
            stmt.setString(2, subjectCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("UserSubjectMapper: couldn't get version of "+userID+", "+subjectCode);
        return -1;
    }

    /**
     * Insert a pair of user ID and subject code
     * @param userID
     * @param subjectCode
     */
    public static void insert(int userID, String subjectCode) {
        final String checkStmt = "SELECT * FROM users_has_subjects WHERE user_id = ? AND subject_code = ?";
        final String insertStmt = "INSERT INTO users_has_subjects (user_id, subject_code) VALUES (?, ?)";
        try {
            Connection dbConnection = new DBConnection().connect();
            // check if instructor with the subjects already exists.
            PreparedStatement stmt = dbConnection.prepareStatement(checkStmt);
            stmt.setInt(1, userID);
            stmt.setString(2, subjectCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                throw new Exception("Entry with user id and subject code already exists in table.");
            }
            stmt = dbConnection.prepareStatement(insertStmt);
            stmt.setInt(1, userID);
            stmt.setString(2, subjectCode);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the fudge points of a subject of a student. Also update total marks if all submission of
     * the student has marks.
     * @param userID The user_id of the student.
     * @param subjectCode The subject_code of the subject.
     * @param fudgePoints The new fudge points.
     * @return True if update is successful.
     */
    public static boolean updateFudgePoints(int userID, String subjectCode, float fudgePoints) {
        // Calculate total marks
        boolean updateTotalMarks = true;
        List<Exam> exams = ExamMapper.getAllExamsWithSubjectCode(subjectCode);
        float totalMarks = 0f;
        for (Exam exam : exams) {
            Submission submission = SubmissionMapper.getSubmissionByIDs(exam.getExamID(), userID);
            if (submission == null || submission.getMarks() == -1) {
                updateTotalMarks = false;
                break;
            }
            totalMarks += submission.getMarks();
        }
        totalMarks += fudgePoints;
        int version = getVersion(userID, subjectCode);
        // Update fudge points (and marks)
        if (updateTotalMarks) {
            final String updateStmt =
                    "UPDATE users_has_subjects SET fudge_points = ?, marks = ?, version = ? WHERE user_id = ? AND subject_code = ?";
            try {
                Connection dbConnection = new DBConnection().connect();
                PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
                stmt.setFloat(1, fudgePoints);
                stmt.setFloat(2, totalMarks);
                stmt.setInt(3, version + 1);
                stmt.setInt(4, userID);
                stmt.setString(5, subjectCode);
                int result = stmt.executeUpdate();
                return (result > 0) ? true : false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            final String updateStmt =
                    "UPDATE users_has_subjects SET fudge_points = ?, version = ? WHERE user_id = ? AND subject_code = ?";
            try {
                Connection dbConnection = new DBConnection().connect();
                PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
                stmt.setFloat(1, fudgePoints);
                stmt.setInt(2, version + 1);
                stmt.setInt(3, userID);
                stmt.setString(4, subjectCode);
                int result = stmt.executeUpdate();
                return (result > 0) ? true : false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Recalculate and update the total marks of a student of a subject.
     * @param userID
     * @param subjectCode
     * @return
     */
    public static boolean updateMarks(int userID, String subjectCode) {
        List<Exam> exams = ExamMapper.getAllExamsWithSubjectCode(subjectCode);
        float totalMarks = 0f;
        for (Exam exam : exams) {
            Submission submission = SubmissionMapper.getSubmissionByIDs(exam.getExamID(), userID);
            if (submission == null || submission.getMarks() == -1) {
                return false;
            }
            totalMarks += submission.getMarks();
        }
        float fudgePoints = getStudentSubjectMark(userID, subjectCode).getFudgePoints();
        totalMarks += fudgePoints;
        int version = getVersion(userID, subjectCode);
        final String updateStmt =
                "UPDATE users_has_subjects SET marks = ?, version = ? WHERE user_id = ? AND subject_code = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
            stmt.setFloat(1, totalMarks);
            stmt.setInt(2, version + 1);
            stmt.setInt(3, userID);
            stmt.setString(4, subjectCode);
            int result = stmt.executeUpdate();
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
