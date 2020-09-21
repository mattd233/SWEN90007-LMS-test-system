package db.mapper;

import db.DBConnection;
import domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StudentMapper extends Mapper{
    public static final String findStudentStmt = "SELECT * FROM users WHERE user_id = ?";

    /**
     *
     * @param studentID
     * @return
     */
    public static Student findStudentWithID(int studentID) {
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findStudentStmt);
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                if (!type.equals("student")) {
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

    public static ArrayList<Integer> findStudentIDs() {
        final String findStudentIDsStmt = "SELECT * FROM users";
        ArrayList<Integer> sIds = new ArrayList<Integer>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findStudentIDsStmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                if (type.equals("STUDENT")) {
                    System.out.println(id);
                    sIds.add(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sIds;
    }
}
