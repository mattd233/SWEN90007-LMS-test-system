package db.mapper;

import db.DBConnection;
import domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentMapper extends Mapper{

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
                if (!type.equals("STUDENT")) {
                    throw new Exception("Not a student.");
                }
                String name = rs.getString(3);
                String userName = rs.getString(4);
                String passWord = rs.getString(5);
                return new Student(id, name, userName, passWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
