package main.java.domain;

public class Choice {

    private int questionID;
    private int choiceID;
    private String choiceDescription;

    public Choice(int choiceID, int questionID, String choiceDescription) {
        this.choiceID = choiceID;
        this.questionID = questionID;
        this.choiceDescription = choiceDescription;
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
        db.ChoiceUOW.getCurrent().registerDirty(this);
    }
}
