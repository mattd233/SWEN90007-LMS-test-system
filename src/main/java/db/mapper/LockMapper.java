package main.java.db.mapper;

import main.java.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LockMapper {

    public static boolean hasKey(int lockable) {
        final String selectStmt = "SELECT FROM locks WHERE lockable = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(selectStmt);
            stmt.setInt(1, lockable);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getOwner(int lockable) {
        final String selectStmt = "SELECT * FROM locks WHERE lockable = ?";

        String owner = null;
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(selectStmt);
            stmt.setInt(1, lockable);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // skip lockable (index = 1)
                owner = rs.getString(2);
                return owner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }

    public static void releaseAllLocksByOwner(String owner) {
        final String deleteStmt = "DELETE FROM locks WHERE owner = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(deleteStmt);
            stmt.setString(1, owner);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(int lockable, String owner) {
        final String insertStmt = "INSERT INTO locks VALUES (?, ?)";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(insertStmt);
            stmt.setInt(1, lockable);
            stmt.setString(2, owner);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int lockable) {
        final String deleteStmt = "DELETE FROM locks WHERE lockable = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(deleteStmt);
            stmt.setInt(1, lockable);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
