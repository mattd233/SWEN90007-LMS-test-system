package main.java.db.mapper;

import main.java.db.DBConnection;
import main.java.domain.Choice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChoiceMapper {

    public static List<Choice> getChoices(int examID, int questionNumber) {
        final String findChoicesStmt = "SELECT * FROM choices WHERE exam_id = ? AND question_number = ?";
        List<Choice> choices = new ArrayList<Choice>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findChoicesStmt);
            stmt.setInt(1, examID);
            stmt.setInt(2, questionNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int choiceNumber = rs.getInt(3);
                String choiceDescription = rs.getString(4);
                choices.add(new Choice(examID, questionNumber, choiceNumber, choiceDescription));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return choices;
    }

    public static void insert(Choice choice) {
        final String insertChoiceStmt = "INSERT INTO choices VALUES (?, ?, ?, ?)";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(insertChoiceStmt);
            stmt.setInt(1, choice.getExamID());
            stmt.setInt(2, choice.getQuestionNumber());
            stmt.setInt(3, choice.getChoiceNumber());
            stmt.setString(4, choice.getChoiceDescription());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
