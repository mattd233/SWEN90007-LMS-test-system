package domain;

import db.mapper.CoordinatorMapper;

import java.util.List;

public class Coordinator {
    private int staffID;
    private String name;
    private String username;
    private String password;

    public Coordinator(int staffID, String name) {
        this.staffID = staffID;
        this.name = name;
    }

    public Coordinator(int staffID, String name, String username, String password) {
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

    public List<Subject> getSubjectWithCoordinator(Coordinator coordinator) {
        return CoordinatorMapper.getSubjectWithCoordinator(this);
    }
}
