package domain;

public abstract class Question {

    public enum QuestionType {
        MULTIPLE_CHOICE,
        SHORT_ANSWER
    }

    private int examID;
    private int questionNumber;
    private String title;
    private String description;
    private int marks;

    public Question(int examID, int questionNumber, String title, String description, int marks) {
        this.examID = examID;
        this.questionNumber = questionNumber;
        this.title = title;
        this.description = description;
        this.marks = marks;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
