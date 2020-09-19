package db.mapper;

import db.DBConnection;
import domain.Coordinator;
import domain.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class CoordinatorMapper {
    public static final String findCoordinatorStmt = "SELECT * FROM coordinators WHERE staff_id = ?";

    public static Coordinator findCoordinatorWithID(int staff_id) {
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findCoordinatorStmt);
            stmt.setInt(1, staff_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int staffID = rs.getInt(1);
                String name = rs.getString(2);
                String userName = rs.getString(3);
                String passWord = rs.getString(4);
                return new Coordinator(staffID, name, userName, passWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Subject> getSubjectWithCoordinator(Coordinator coordinator) {
        return null;
    }
}
