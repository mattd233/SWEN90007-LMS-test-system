package main.java.controller.instructor;

import main.java.db.mapper.ChoiceMapper;
import main.java.db.mapper.QuestionMapper;
import main.java.domain.Choice;
import main.java.domain.MultipleChoiceQuestion;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Instructor/saveExam")
public class AddExamQuestionsController extends HttpServlet {

    public AddExamQuestionsController(){
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
