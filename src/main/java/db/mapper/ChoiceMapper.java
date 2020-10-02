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

    public static List<Choice> getChoices(int questionID) {
        final String findChoicesStmt = "SELECT * FROM choices WHERE question_id = ?";
        List<Choice> choices = new ArrayList<Choice>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findChoicesStmt);
            stmt.setInt(1, questionID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int choiceID = rs.getInt(1);
                String choiceDescription = rs.getString(3);
                choices.add(new Choice(choiceID, questionID, choiceDescription));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return choices;
    }

    public static void insert(Choice choice) {
        final String insertChoiceStmt = "INSERT INTO choices VALUES (DEFAULT, ?, ?)";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(insertChoiceStmt);
            stmt.setInt(1, choice.getQuestionID());
            stmt.setString(2, choice.getChoiceDescription());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Choice choice) {
        final String updateChoiceStmt = "UPDATE choices SET choice_description = ? WHERE choice_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(updateChoiceStmt);
            stmt.setString(1, choice.getChoiceDescription());
            stmt.setInt(2, choice.getChoiceID());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Choice choice) {
        final String deleteChoiceStmt = "DELETE FROM choices where choice_id = ?";
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(deleteChoiceStmt);
            stmt.setInt(1, choice.getChoiceID());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
