package main.java.domain;

import main.java.db.ChoiceUOW;

public class Choice {

    private int questionID;
    private int choiceID;
    private String choiceDescription;

    // pulled the item from database
    public Choice(int choiceID, int questionID, String choiceDescription) {
        this.choiceID = choiceID;
        this.questionID = questionID;
        this.choiceDescription = choiceDescription;
    }

    // create choice to be added to the database
    public Choice(int questionID, String choiceDescription) {
        this.questionID = questionID;
        this.choiceDescription = choiceDescription;
        ChoiceUOW.getCurrent().registerNew(this);
    }

    public int getQuestionID() {
        return questionID;
    }

    public int getChoiceID() {
        return choiceID;
    }

    public String getChoiceDescription() {
        return choiceDescription;
    }

    public void setChoiceDescription(String choiceDescription) {
        this.choiceDescription = choiceDescription;
        ChoiceUOW.getCurrent().registerDirty(this);
    }
}
