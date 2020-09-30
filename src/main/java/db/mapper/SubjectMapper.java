package main.java.db.mapper;

import main.java.db.DBConnection;
import main.java.domain.Exam;
import main.java.domain.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SubjectMapper extends Mapper {


    /**
     *
     * @return
     */
    public static List<Subject> getAllSubjects() {
        final String findAllSubjectsStatement =
                "SELECT s.subject_code, s.name, u.user_id, u.name FROM subjects s\n" +
                "INNER JOIN users_has_subjects uhs on s.subject_code = uhs.subject_code\n" +
                "INNER JOIN users u on uhs.user_id = u.user_id\n" +
                "WHERE u.type = 'instructor'";

        // A hashmap that uses subject code as key and the object itself as value
        // It is used to determine whether a subject has already been instantiated
        HashMap<String, Subject> subjects = new HashMap<>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findAllSubjectsStatement);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String code = rs.getString(1);
                String subjectName = rs.getString(2);
                int staffID = rs.getInt(3);
                String coordinatorName = rs.getString(4);

                // new subject
                if (!subjects.containsKey(code)) {
                    Subject newSubject = new Subject(code, subjectName);
                    newSubject.addCoordinator(staffID, coordinatorName);
                    subjects.put(code, newSubject);
                } else {
                    Subject existingSubject = subjects.get(code);
                    existingSubject.addCoordinator(staffID, coordinatorName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(subjects.values());
    }

    public static void insert(Subject subject) {
        final String insertSubjectStmt = "INSERT INTO subjects VALUES (?, ?)";
        final String insertCHSStmt = "INSERT INTO coordinators_has_subjects VALUES (?, ?)";

//        try {
//            // first check if we can find the coordinator with given id
//            for (Integer staffID : subject.getCoordinatorIDs()) {
//                Coordinator coordinator = CoordinatorMapper.findCoordinatorWithID(staffID);
//                if (coordinator == null) {
//                    throw new Exception("Coordinator does not exist");
//                }
//                if (!coordinator.getName().equals(subject.getCoordinatorNameByID(staffID))) {
//                    throw new Exception("Unmatched  coordinator_id and name.");
//                }
//            }
//
//            Connection dbConnection = new DBConnection().connect();
//            PreparedStatement insertStatement = dbConnection.prepareStatement(insertSubjectStmt);
//            insertStatement.setString(1,subject.getSubjectCode());
//            insertStatement.setString(2, subject.getSubjectName());
//            insertStatement.execute();
//
//
//
//            insertStatement = dbConnection.prepareStatement(insertCHSStmt);
//            insertStatement.setInt(1, coordinator.getStaffID());
//            insertStatement.setString(2, subject.getSubjectCode());
//            insertStatement.execute();
//
//
//            System.out.println("Insertion successful.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Get all subjects being taught by a single instructor
     * @param userID
     * @return
     */
    public static List<Subject> getAllSubjectsWithInstructor(int userID) {

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

    /**
     * Get all exams under a given subject
     * @param subjectCode
     * @return
     */
    public static List<Exam> getAllExamsWithSubject(String subjectCode) {

        final String findExamsStmt=  "SELECT * FROM exams WHERE subject_code = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findExamsStmt);
            stmt.setString(1, subjectCode);
            ResultSet rs = stmt.executeQuery();
            List<Exam> exams = new ArrayList<>();
            while (rs.next()) {
                int examID = rs.getInt(1);
                String code = rs.getString(2);
                String title = rs.getString(3);
                String description = rs.getString(4);
                String statusString = rs.getString(5);
                Exam.ExamStatus status =  Exam.ExamStatus.valueOf(Exam.ExamStatus.class, statusString);
                exams.add(new Exam(examID, code ,title, description, status));
            }
            return exams;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
