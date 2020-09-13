import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Program {

    private static final String findPassWithUsername = "SELECT password FROM students WHERE username = ";

    public static void main(String args[]) throws Exception {
        String username = "'123'";
        Connection connection = new db.DBConnection().connect();
        PreparedStatement stmt = connection.prepareStatement(findPassWithUsername + username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            String password = rs.getString("password");
            System.out.println(password);
        } else {
            throw new Exception("Username doesn't exist.");
        }

    }

}
