package main.java.domain;

public class Student extends User {

    public Student(int studentID, String name, String username, String password) {
        super(studentID, name, username, password);
    }

    public int getStudentID() {
        return super.getUserID();
    }

    public void setStudentID(int studentID) {
        super.setUserID(studentID);
    }

}
