package db.mapper;

import db.DBConnection;
import domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserSubjectMapper {

    public static List<Student> getAllStudentsWithSubject(String subjectCode) {
        final String getStudentsStmt =
                "SELECT * FROM users_has_subjects WHERE subject_code = ?";
        List<Student> students = new ArrayList<Student>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(getStudentsStmt);
            stmt.setString(1, subjectCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int sID = rs.getInt(1);
                Student student = StudentMapper.findStudentWithID(sID);
                if (student != null) students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public static float getFudgePoints(int userID, String subjectCode) {
        final String getFudgePointsStmt =
                "SELECT * FROM users_has_subjects WHERE user_id = ? AND subject_code = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(getFudgePointsStmt);
            stmt.setInt(1, userID);
            stmt.setString(2, subjectCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean updateFudgePoints(int userID, String subjectCode, float fudgePoints) {
        final String updateStmt =
                "UPDATE users_has_subjects SET fudge_points = ? WHERE user_id = ? AND subject_code = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateStmt);
            stmt.setFloat(1, fudgePoints);
            stmt.setInt(2, userID);
            stmt.setString(3, subjectCode);
            int result = stmt.executeUpdate();
            return (result > 0) ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
