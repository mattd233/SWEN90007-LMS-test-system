package main.java.domain;

import java.sql.Timestamp;

public class Submission {

    private int examID;
    private int userID;
    private Timestamp submissionTime;
    private boolean isMarked;
    private Float marks;
    private Float fudgePoints;

    public Submission(int examID, int userID) {
        this.examID = examID;
        this.userID = userID;
        this.submissionTime = new Timestamp(System.currentTimeMillis());
        this.isMarked = false;
        this.marks = null;
        this.fudgePoints = 0.0f;
    }

    public Submission(int examID, int userID, Timestamp submissionTime, boolean isMarked, float marks, float fudgePoints) {
        this.examID = examID;
        this.userID = userID;
        this.submissionTime = submissionTime;
        this.isMarked = isMarked;
        this.marks = marks;
        this.fudgePoints = fudgePoints;
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
