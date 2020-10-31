package main.java.db.mapper;

import main.java.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LockMapper {

    public static boolean hasKey(int lockable) {
        final String selectStmt = "SELECT * FROM exam_locks WHERE lockable = ?";
        boolean isLocked = false;
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(selectStmt);
            stmt.setInt(1, lockable);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isLocked = true;
            }
            // Close connection
            dbConnection.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isLocked;
    }

    public static String getOwner(int lockable) {
        final String selectStmt = "SELECT * FROM exam_locks WHERE lockable = ?";
        String owner = null;
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(selectStmt);
            stmt.setInt(1, lockable);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // skip lockable (index = 1)
                owner = rs.getString(2);
            }
            // Close connection
            dbConnection.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }

    public static void releaseAllLocksByOwner(String owner) {
        final String deleteStmt = "DELETE FROM exam_locks WHERE owner = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(deleteStmt);
            stmt.setString(1, owner);
            stmt.execute();
            // Close connection
            dbConnection.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(int lockable, String owner) {
        final String insertStmt = "INSERT INTO exam_locks VALUES (?, ?)";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(insertStmt);
            stmt.setInt(1, lockable);
            stmt.setString(2, owner);
            stmt.execute();
            // Close connection
            dbConnection.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int lockable) {
        final String deleteStmt = "DELETE FROM exam_locks WHERE lockable = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(deleteStmt);
            stmt.setInt(1, lockable);
            stmt.execute();
            // Close connection
            dbConnection.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
