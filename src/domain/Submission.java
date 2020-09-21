package domain;

import java.sql.Timestamp;

public class Submission {
    private int examID;
    private int userID;
    private Timestamp submissionTime;
    private boolean isMarked;
    private float marks;

    public Submission(int examID, int userID, Timestamp submissionTime, boolean isMarked, float marks) {
        this.examID = examID;
        this.userID = userID;
        this.submissionTime = submissionTime;
        this.isMarked = isMarked;
        this.marks = marks;
    }
}
