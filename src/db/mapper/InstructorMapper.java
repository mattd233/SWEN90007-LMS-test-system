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
                if (!type.equals("INSTRUCTOR")) {
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

}
