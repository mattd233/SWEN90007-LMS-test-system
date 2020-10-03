package main.java;
import main.java.db.QuestionUOW;
import main.java.db.mapper.ExamMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.db.mapper.SubjectMapper;
import main.java.domain.Exam;
import main.java.domain.MultipleChoiceQuestion;
import main.java.domain.Question;
import main.java.domain.Subject;

import java.util.List;

// This is for testing code, ignore this class.
public class Program {


    public static void main(String args[]) throws Exception {
        for (Subject subject: SubjectMapper.getAllSubjects()) {
            System.out.println(subject.getSubjectCode() + " " + subject.getInstructorNamesAsOneString());
        }
    }


}
