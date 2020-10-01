package domain;

import java.sql.Timestamp;

public class Submission {

    private int examID;
    private int userID;
    private Timestamp submissionTime;
    private boolean isMarked;
    private float marks;
    private float fudgePoints;

    public Submission(int examID, int userID, Timestamp submissionTime, boolean isMarked, float marks, float fudgePoints) {
        this.examID = examID;
        this.userID = userID;
        this.submissionTime = submissionTime;
        this.isMarked = isMarked;
        this.marks = marks;
        this.fudgePoints = fudgePoints;
    }

    public Submission(int examID, int studentID, Timestamp time) {
        this.examID = examID;
        this.userID = studentID;
        this.submissionTime = time;
        isMarked = false;
        marks = -1;
        fudgePoints = -1;
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

    public Timestamp getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(Timestamp submissionTime) {
        this.submissionTime = submissionTime;
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

    public float getFudgePoints() {
        return fudgePoints;
    }

    public void setFudgePoints(float fudgePoints) {
        this.fudgePoints = fudgePoints;
    }
}
