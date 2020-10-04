package main.java;
import main.java.db.mapper.SubjectMapper;
import main.java.domain.Subject;

// This is for testing code, ignore this class.
public class Program {


    public static void main(String args[]) throws Exception {
        for (Subject subject: SubjectMapper.getAllSubjects()) {
            System.out.println(subject.getSubjectCode() + " " + subject.getInstructorNamesAsOneString());
        }
    }


}
