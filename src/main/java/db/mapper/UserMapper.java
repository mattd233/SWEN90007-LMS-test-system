package main.java.db.mapper;

import main.java.db.DBConnection;
import main.java.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User getUserWithUsernamePassword(String username, String password) {
        final String findUserStmt = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findUserStmt);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt(1);
                String userType = rs.getString(2);
                String name = rs.getString(3);
                if (userType.equals(User.UserType.ADMIN.toString())) {
                    return new Admin(userID, name, username, password);
                } else if (userType.equals(User.UserType.INSTRUCTOR.toString())) {
                    return new Instructor(userID, name, username, password);
                } else if (userType.equals(User.UserType.STUDENT.toString())) {
                    return new Student(userID, name, username, password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find the corresponding row in instructors with the given staff_id.
     * @param userID
     * @return A new instructor object.
     */
    public static Instructor findInstructorWithID(int userID) {

        final String findInstructorStmt = "SELECT * FROM users WHERE user_id = ?";

        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findInstructorStmt);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                if (type.equals(User.UserType.INSTRUCTOR.toString())) {
                    String name = rs.getString(3);
                    String userName = rs.getString(4);
                    String passWord = rs.getString(5);
                    return new Instructor(id, name, userName, passWord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param studentID
     * @return
     */
    public static Student findStudentWithID(int studentID) {
        final String findStudentStmt = "SELECT * FROM users WHERE user_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findStudentStmt);
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                if (type.equals(User.UserType.STUDENT.toString())) {
                    String name = rs.getString(3);
                    String userName = rs.getString(4);
                    String passWord = rs.getString(5);
                    return new Student(id, name, userName, passWord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get all the subjects a student enrolled with the input studentID
     * @param userID
     * @return subjects
     */
    public static List<Subject> getStudentEnrolledSubject(int userID) {
        final String findSubjectsStmt= "SELECT s.subject_code, s.name FROM subjects s\n" +
                "INNER JOIN users_has_subjects uhs on s.subject_code = uhs.subject_code\n" +
                "WHERE user_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findSubjectsStmt);
            stmt.setInt(1, userID);
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
