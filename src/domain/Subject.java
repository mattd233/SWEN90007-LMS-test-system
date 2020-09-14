package domain;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.JDBCType.VARCHAR;
import static java.text.Collator.PRIMARY;


public class Subject {
    private String subjectCode;
    private String name;
    private String coordinatorName;

    public Subject (String subjectCode, String name, String coordinatorName) {
        this.subjectCode = subjectCode;
        this.name = name;
        this.coordinatorName = coordinatorName;
    };

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getName() {
        return name;
    }

    public String getCoordinator() {
        return coordinatorName;
    }

    private static final String findAllSubjectsStatement =
            "SELECT s.subject_code, s.name, c.name FROM subjects s\n" +
            "INNER JOIN coordinators_has_subjects chs on s.subject_code = chs.subject_code\n" +
            "INNER JOIN coordinators c on chs.staff_id = c.staff_id";

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
                subjects.add(new Subject(code,name,coordinator));
            }
        } catch (SQLException e) { }
        return subjects;
    }

    private String insertSubjectStatement = "INSERT INTO subjects VALUES (?, ?, ?)";

    public String insert() {
        try {
            Connection DBConnection = new DBConnection().connect();
            System.out.println("Hello");
            PreparedStatement insertStatement = DBConnection.prepareStatement(insertSubjectStatement);
            insertStatement.setString(1,subjectCode);
            insertStatement.setString(2, name);
//            insertStatement.setString(3, coordinator.getName());
            insertStatement.execute();
            System.out.println("Insertion successful.");
        } catch (SQLException e) {}
        return getSubjectCode();
    }
}