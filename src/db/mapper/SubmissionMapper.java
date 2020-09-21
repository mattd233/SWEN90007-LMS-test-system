package db.mapper;

import db.DBConnection;
import domain.Submission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class SubmissionMapper extends Mapper {
    public static Submission findSubmissionById(int examID, int userID) {
        final String findSubmissionStmt = "SELECT * FROM submissions WHERE exam_id = ? AND user_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findSubmissionStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int eId = rs.getInt(1);
                int uId = rs.getInt(2);
                Timestamp submissionTime = rs.getTimestamp(3);
                Boolean isMarked = rs.getBoolean(4);
                float marks = rs.getFloat(5);
                return new Submission(eId, uId, submissionTime, isMarked, marks);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
