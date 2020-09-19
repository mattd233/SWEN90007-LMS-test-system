package db.mapper;

import db.DBConnection;
import domain.Instructor;
import domain.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InstructorMapper extends Mapper{


    /**
     * Find the corresponding row in instructors with the given staff_id.
     * @param staffID
     * @return A new instructor object.
     */
    public static Instructor findInstructorWithID(int staffID) {

        final String findInstructorStmt = "SELECT * FROM users WHERE user_id = ?";

        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findInstructorStmt);
            stmt.setInt(1, staffID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                if (!type.equals("instructor")) {
                    throw new Exception("Not an instructor.");
                }
                String name = rs.getString(3);
                String userName = rs.getString(4);
                String passWord = rs.getString(5);
                return new Instructor(id, name, userName, passWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Get all subjects being taught by a single instructor
     * @param instructor
     * @return
     */
    public static List<Subject> getAllSubjectsWithInstructor(Instructor instructor) {

        final String findSubjectsStmt= "SELECT s.subject_code, s.name FROM subjects s\n" +
                        "INNER JOIN users_has_subjects uhs on s.subject_code = uhs.subject_code\n" +
                        "WHERE user_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findSubjectsStmt);
            stmt.setInt(1, instructor.getStaffID());
            ResultSet rs = stmt.executeQuery();
            List<Subject> subjects = new ArrayList<>();
            while (rs.next()) {
                String subject_code = rs.getString(1);
                String name = rs.getString(2);
                subjects.add(new Subject(subject_code, name));
            }
            return subjects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
