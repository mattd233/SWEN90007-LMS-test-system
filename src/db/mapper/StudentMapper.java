package db.mapper;

import db.DBConnection;
import domain.Student;
import domain.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentMapper extends Mapper{

    public static List<Student> getAllStudents() {
        final String findStudentIDsStmt = "SELECT * FROM users";
        List<Student> students = new ArrayList<Student>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findStudentIDsStmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString(2);
                if (!type.equals("STUDENT")) {
                    continue;
                }
                int id = rs.getInt(1);
                String name = rs.getString(3);
                String userName = rs.getString(4);
                String passWord = rs.getString(5);
                students.add(new Student(id, name, userName, passWord));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     *
     * @param studentID
     * @return
     */
    public static Student findStudentWithID(int studentID) {
        final String findStudentStmt = "SELECT * FROM users WHERE user_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findStudentStmt);
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String type = rs.getString(2);
                if (!type.equals("STUDENT")) {
                    throw new Exception("Not a student.");
                }
                String name = rs.getString(3);
                String userName = rs.getString(4);
                String passWord = rs.getString(5);
                return new Student(id, name, userName, passWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get all the subjects a student enrolled with the input studentID
     * @param userID
     * @return subjects
     */
    public static List<Subject> getStudentEnrolledSubject(int userID) {
        final String findSubjectsStmt= "SELECT s.subject_code, s.name FROM subjects s\n" +
                "INNER JOIN users_has_subjects uhs on s.subject_code = uhs.subject_code\n" +
                "WHERE user_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findSubjectsStmt);
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            List<Subject> subjects = new ArrayList<>();
            while (rs.next()) {
                String subject_code = rs.getString(1);
                String name = rs.getString(2);
                subjects.add(new Subject(subject_code, name));
            }
            return subjects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
