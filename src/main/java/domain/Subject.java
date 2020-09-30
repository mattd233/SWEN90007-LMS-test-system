package main.java.domain;

import main.java.db.mapper.InstructorMapper;

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

    public void addCoordinator(int staffID, String name){
        subjectCoordinators.put(staffID, name);
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return name;
    }

    public String getCoordinatorNameByID(int staffID) {
        return subjectCoordinators.get(staffID);
    }

    public List<Integer> getCoordinatorIDs() {
        return new ArrayList<Integer>(subjectCoordinators.keySet());
    }

    public String getCoordinatorNamesAsOneString() {
        assert (subjectCoordinators != null);
        List<Integer> coordinatorIDs = getCoordinatorIDs();
        String output = subjectCoordinators.get(coordinatorIDs.get(0));
        for (int i=1; i<subjectCoordinators.size(); i++) {
            output += ", " + subjectCoordinators.get(coordinatorIDs.get(i));
        }
        return output;
    }

    public List<Instructor> getSubjectCoordinators() {
        List<Instructor> instructors = new ArrayList<>();
        for (int staffID : subjectCoordinators.keySet()) {
            Instructor instructor = InstructorMapper.findInstructorWithID(staffID);
            instructors.add(instructor);
        }
        return instructors;
    }

}