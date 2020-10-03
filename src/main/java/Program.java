package main.java;
import main.java.db.QuestionUOW;
import main.java.db.mapper.ExamMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.domain.Exam;
import main.java.domain.MultipleChoiceQuestion;
import main.java.domain.Question;

import java.util.List;

// This is for testing code, ignore this class.
public class Program {


    public static void main(String args[]) throws Exception {
        Question question = new MultipleChoiceQuestion(1, 1, "","",0);
        QuestionMapper.delete(question);
    }


}
