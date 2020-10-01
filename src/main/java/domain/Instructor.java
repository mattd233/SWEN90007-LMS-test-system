package main.java.domain;

import main.java.db.mapper.SubjectMapper;

import java.util.List;

public class Instructor extends User {

    public Instructor(int staffID, String name, String username, String password) {
        super(staffID, name, username, password);
    }

    public int getStaffID() {
        return super.getUserID();
    }

    public void setStaffID(int staffID) {
        super.setUserID(staffID);
    }

//    public List<Subject> getSubjectWithCoordinator(int userID) {
//        return SubjectMapper.getAllSubjectsWithInstructor(userID);
//    }
}
