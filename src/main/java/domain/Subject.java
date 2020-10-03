package main.java.domain;

import main.java.db.mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Subject {
    private String subjectCode;
    private String name;
    // hashmap that uses the staff_ids of coordinators as key and their names as value
    private HashMap<Integer, String> subjectCoordinators;

    public Subject (String subjectCode, String name) {
        this.subjectCode = subjectCode;
        this.name = name;
        subjectCoordinators = new HashMap<>();
    };

    public void addInstructor(int staffID, String name){
        subjectCoordinators.put(staffID, name);
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return name;
    }

    public String getInstructorNameByID(int staffID) {
        return subjectCoordinators.get(staffID);
    }

    public List<Integer> getInsturctorIDs() {
        return new ArrayList<Integer>(subjectCoordinators.keySet());
    }

    // Format the name of the instructor as one string
    // For example if the instructor names are Maria and Eduardo, the formatted string would be "Maria, Eduardo"
    public String getInstructorNamesAsOneString() {
        assert (subjectCoordinators != null);
        List<Integer> coordinatorIDs = getInsturctorIDs();
        String output = subjectCoordinators.get(coordinatorIDs.get(0));
        for (int i=1; i<subjectCoordinators.size(); i++) {
            output += ", " + subjectCoordinators.get(coordinatorIDs.get(i));
        }
        return output;
    }

    // get the instructors for a subject
    public List<Instructor> getSubjectInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        for (int staffID : subjectCoordinators.keySet()) {
            Instructor instructor = UserMapper.findInstructorWithID(staffID);
            instructors.add(instructor);
        }
        return instructors;
    }

}