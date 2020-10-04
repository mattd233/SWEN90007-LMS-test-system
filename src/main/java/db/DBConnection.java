package main.java.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String url = "jdbc:postgresql://localhost:5432/myDB";
    private final String user = "postgres";
    private final String password = "789456";
//    private final String url = "jdbc:postgresql://ec2-34-233-43-35.compute-1.amazonaws.com:5432/ddh3poiu9vi13m";
//    private final String user = "fwzqdnbmuhgibe";
//    private final String password = "373bbd387e14cf816759d1a16472798120cbc0e0362379319c2bd7d11152f889";

    /**
     * Connect to the PostgreSQL database
     * @return a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            DriverManager.registerDriver(new org.postgresql.Driver());
            // String dbUrl = System.getenv("JDBC_DATABASE_URL");
            // conn = DriverManager.getConnection(dbUrl);
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.err.println("Failed to make connection!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

}