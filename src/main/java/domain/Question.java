package main.java.domain;

public abstract class Question {

    public enum QuestionType {
        MULTIPLE_CHOICE,
        SHORT_ANSWER
    }

    private int questionID;
    private int examID;
    private QuestionType questionType;
    private String title;
    private String description;
    private int marks;

    // get a question object from database, do not register as new
    public Question(int questionID, int examID, QuestionType questionType, String title, String description, int marks) {
        this.questionID = questionID;
        this.examID = examID;
        this.questionType = questionType;
        this.title = title;
        this.description = description;
        this.marks = marks;
    }

    // Create a new Question object
    public Question(int examID, QuestionType questionType, String title, String description, int marks) {
        this.examID = examID;
        this.questionType = questionType;
        this.title = title;
        this.description = description;
        this.marks = marks;
        db.QuestionUOW.getCurrent().registerNew(this);
    }

    public int getExamID() {
        return examID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
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
