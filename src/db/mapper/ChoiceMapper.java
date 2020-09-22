package db.mapper;

import db.DBConnection;
import domain.Choice;
import domain.SubmittedQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChoiceMapper {

    public static List<Choice> getChoices(int examID, int questionNumber) {
        final String findAnswerStmt = "SELECT * FROM choices WHERE exam_id = ? AND question_number = ?";
        List<Choice> choices = new ArrayList<Choice>();
        try {
            Connection dbConnection = new DBConnection().connect();
            PreparedStatement stmt = dbConnection.prepareStatement(findAnswerStmt);
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

}
