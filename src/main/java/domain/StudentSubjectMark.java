package main.java.domain;

public class StudentSubjectMark {

    private int userId;
    private String subjectCode;
    private float fudgePoints;
    private float totalMarks;
    private int version;

    public StudentSubjectMark(int userId, String subjectCode, float fudgePoints, float totalMarks, int version) {
        this.userId = userId;
        this.subjectCode = subjectCode;
        this.fudgePoints = fudgePoints;
        this.totalMarks = totalMarks;
        this.version = version;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public float getFudgePoints() {
        return fudgePoints;
    }

    public void setFudgePoints(float fudgePoints) {
        this.fudgePoints = fudgePoints;
    }

    public float getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(float totalMarks) {
        this.totalMarks = totalMarks;
    }

    public int getVersion() {
        return version;
    }

}
