package main.java.domain;


public class Choice {

    private int examID;
    private int questionNumber;
    private int choiceNumber;
    private String choiceDescription;

    public Choice(int examID, int questionNumber, int choiceNumber, String choiceDescription) {
        this.examID = examID;
        this.questionNumber = questionNumber;
        this.choiceNumber = choiceNumber;
        this.choiceDescription = choiceDescription;
    }

    public int getExamID() {
        return examID;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public int getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(int choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public String getChoiceDescription() {
        return choiceDescription;
    }

    public void setChoiceDescription(String choiceDescription) {
        this.choiceDescription = choiceDescription;
    }
}
