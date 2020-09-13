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
    private Instructor coordinator;
    public Subject (String subjectCode, String name, String coordinatorName) {
        this.subjectCode = subjectCode;
        this.coordinator = new Instructor(coordinatorName);
        this.name = name;
    };

    public String getName() {
        return name;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getCoordinator() {
        return coordinator.getName();
    }

    private static final String findAllSubjectsStatement = "SELECT * from subjects";

    public static List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        try {
            Connection DBConnection = new DBConnection().connect();
            PreparedStatement stmt = DBConnection.prepareStatement(findAllSubjectsStatement);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                String coordinator = rs.getString("coordinator");
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
            insertStatement.setString(3, coordinator.getName());
            insertStatement.execute();
            System.out.println("Insertion successful.");
        } catch (SQLException e) {}
        return getSubjectCode();
    }
}