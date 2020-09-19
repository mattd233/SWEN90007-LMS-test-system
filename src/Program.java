import db.DBConnection;

import java.sql.Connection;

public class Program {

    private static final String findPassWithUsername = "SELECT password FROM students WHERE username = ";



    public static void main(String args[]) throws Exception {
        Connection conn = new DBConnection().connect();
    }


}
