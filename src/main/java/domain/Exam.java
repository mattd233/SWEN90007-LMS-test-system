package main.java.domain;

public class Exam {

    public enum ExamStatus {
        PUBLISHED,
        UNPUBLISHED,
        CLOSED
    }

    private int examID;
    private String subjectCode;
    private String title;
    private String description;
    private ExamStatus status;

    public Exam(int examID, String subjectCode, String title, String description, ExamStatus status) {
        this.examID = examID;
        this.subjectCode = subjectCode;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Exam(String subjectCode, String title, String description) {
        examID = -1;
        this.subjectCode = subjectCode;
        this.title = title;
        this.description = description;
        this.status = ExamStatus.UNPUBLISHED;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
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

    public ExamStatus getStatus() {
        return status;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }
}
