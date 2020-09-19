package db.mapper;

import db.DBConnection;
import domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentMapper {
    public static final String findStudentStmt = "SELECT * FROM students WHERE student_id = ?";

    public static Student findStudentWithID(int studentID) {
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findStudentStmt);
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String userName = rs.getString(3);
                String passWord = rs.getString(4);
                return new Student(id, name, userName, passWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
