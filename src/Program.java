import db.DBConnection;
import db.mapper.CoordinatorMapper;
import db.mapper.StudentMapper;
import db.mapper.SubjectMapper;
import domain.Coordinator;
import domain.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Program {

    private static final String findPassWithUsername = "SELECT password FROM students WHERE username = ";



    public static void main(String args[]) throws Exception {
        Connection conn = new DBConnection().connect();
    }


}
