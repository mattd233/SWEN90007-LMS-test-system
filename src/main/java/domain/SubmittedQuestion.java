package domain;

public class SubmittedQuestion {

    private int examID;
    private int userID;
    private int questionNumber;
    private String questionType;
    private int choiceNumber;
    private String shortAnswer;
    private boolean isMarked;
    private float marks;

    public SubmittedQuestion(int examID, int userID, int questionNumber, String questionType, int choiceNumber, String shortAnswer, boolean isMarked, float marks) {
        this.examID = examID;
        this.userID = userID;
        this.questionNumber = questionNumber;
        this.questionType = questionType;
        this.choiceNumber = choiceNumber;
        this.shortAnswer = shortAnswer;
        this.isMarked = isMarked;
        this.marks = marks;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(int choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public String getShortAnswer() {
        return shortAnswer;
    }

    public void setShortAnswer(String shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public float getMarks() {
        return marks;
    }

    public void setMarks(float marks) {
        this.marks = marks;
    }
}
