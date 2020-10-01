package main.java;
import main.java.db.mapper.ExamMapper;
import main.java.domain.Exam;

// This is for testing code, ignore this class.
public class Program {


    public static void main(String args[]) throws Exception {
        Exam exam = new Exam("SWEN90009", "Sam's exam", "stupid exam");
        System.out.println(ExamMapper.insert(exam));

    }


}
