import db.DBConnection;
import db.mapper.InstructorMapper;
import domain.Instructor;
import domain.Subject;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Program {

    private static final String findPassWithUsername = "SELECT password FROM students WHERE username = ";



    public static void main(String args[]) throws Exception {
        final String findInstructorStmt = "SELECT * FROM users WHERE username = ?";

        // get username and password from the form
        String username = "simaid";
        String password = "000000";
        try {
            // get the password for the entered username
            Connection connection = new db.DBConnection().connect();
            PreparedStatement stmt = connection.prepareStatement(findInstructorStmt);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt("user_id");
                String type = rs.getString("type");
                String returnedPassword = rs.getString("password");
                System.out.println(userID + " " + type + " " + returnedPassword);
            }
        } catch (SQLException e) {}
    }


}
