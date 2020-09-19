package domain;

import db.mapper.InstructorMapper;

import java.util.List;

public class Instructor {
    private int staffID;
    private String name;
    private String username;
    private String password;

    public Instructor(int staffID, String name) {
        this.staffID = staffID;
        this.name = name;
    }

    public Instructor(int staffID, String name, String username, String password) {
        this.staffID = staffID;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
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

    public List<Subject> getSubjectWithCoordinator(Instructor instructor) {
        return InstructorMapper.getAllSubjectsWithInstructor(this);
    }
}
