import db.DBConnection;
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

    private static final String findAllSubjectsStatement =
            "SELECT s.subject_code, s.name, c.name FROM subjects s\n" +
            "INNER JOIN coordinators_has_subjects chs on s.subject_code = chs.subject_code\n" +
            "INNER JOIN coordinators c on chs.staff_id = c.staff_id";

    public static void main(String args[]) throws Exception {
        getAllSubjects();

    }

    public static List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        try {
            Connection DBConnection = new DBConnection().connect();
            PreparedStatement stmt = DBConnection.prepareStatement(findAllSubjectsStatement);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString(1);
                String name = rs.getString(2);
                String coordinator = rs.getString(3);
                System.out.println(code + " " + name + " " + coordinator);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

}
