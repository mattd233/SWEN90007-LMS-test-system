package domain;

public class Student {
    private int studentID;
    private String name;
    private String username;
    private String password;

    public Student(int studentID, String name, String username, String password) {
        this.studentID = studentID;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
