package main.java.db.mapper;

import main.java.db.DBConnection;
import main.java.domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserMapper extends Mapper {

    /**
     * Get the user with the given username and password, return null if the user cannot be found.
     * @param username username
     * @return a user object of either type (student, admin, instructor)
     */
    public static User getUserWithUsername(String username) {
        final String findUserStmt = "SELECT * FROM users WHERE username = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findUserStmt);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt(1);
                String userType = rs.getString(2);
                String name = rs.getString(3);
                String password = rs.getString(5);
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
     * @return A new instructor object if instructor with given id exist and null otherwise.
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
     * Find the corresponding row in instructors with the given name.
     * @param name name of instructor.
     * @return A new instructor object if instructor with given name exist and null otherwise.
     */
    public static Instructor findInstructorWithName(String name) {

        final String findInstructorStmt = "SELECT * FROM users WHERE name = ?";

        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findInstructorStmt);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                if (type.equals(User.UserType.INSTRUCTOR.toString())) {
                    // skip name
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
     * Find the corresponding row in users with the given id.
     * @param studentID id of the student.
     * @return a student object if a student with the given id exists and null otherwise.
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

}
