import db.DBConnection;
import db.mapper.InstructorMapper;
import domain.Instructor;
import domain.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class Program {

    private static final String findPassWithUsername = "SELECT password FROM students WHERE username = ";



    public static void main(String args[]) throws Exception {
        final String findInstructorStmt = "SELECT * FROM users WHERE user_id = ?";

        int staff_id = 1;
        Connection dbConnection = new DBConnection().connect();
        PreparedStatement stmt = dbConnection.prepareStatement(findInstructorStmt);
        stmt.setInt(1, staff_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int staffID = rs.getInt(1);
            String type = rs.getString(2);
            String name = rs.getString(3);
            String userName = rs.getString(4);
            String passWord = rs.getString(5);
            System.out.println(staffID + " " + type + " " + name + " " + userName + " " + passWord);
        }

    }


}
