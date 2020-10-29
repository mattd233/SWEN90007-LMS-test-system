package main.java.db.mapper;

import main.java.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubmissionLockMapper extends Mapper {

    public static boolean hasKey(int examID, int studentID) {
        final String selectStmt =
                "SELECT * FROM submission_locks WHERE exam_id = ? AND student_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(selectStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getOwner(int examID, int studentID) {
        final String selectStmt =
                "SELECT * FROM submission_locks WHERE exam_id = ? AND student_id = ?";
        String owner = null;
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(selectStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                owner = rs.getString(3);
                return owner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }

    public static void releaseAllLocksByOwner(String owner) {
        final String deleteStmt = "DELETE FROM submission_locks WHERE owner = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(deleteStmt);
            stmt.setString(1, owner);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(int examID, int studentID, String owner) {
        final String insertStmt =
                "INSERT INTO submission_locks VALUES (?, ?, ?)";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(insertStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, studentID);
            stmt.setString(3, owner);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int examID, int studentID) {
        final String deleteStmt =
                "DELETE FROM submission_locks WHERE exam_id = ? AND student_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(deleteStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, studentID);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
