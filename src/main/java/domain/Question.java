package main.java.domain;


import main.java.db.QuestionUOW;

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

    // Pulling a question from database, do not register as New
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

//    public void setExamID(int examID) {
//        this.examID = examID;
//        QuestionUOW.getCurrent().registerDirty(this);
//    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
        QuestionUOW.getCurrent().registerDirty(this);
    }
  
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        QuestionUOW.getCurrent().registerDirty(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        QuestionUOW.getCurrent().registerDirty(this);
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
        QuestionUOW.getCurrent().registerDirty(this);
    }


}
