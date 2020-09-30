package main.java.controller;

import main.java.db.mapper.ExamMapper;
import main.java.domain.Exam;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Instructor/createNewExam")
public class AddExamController extends HttpServlet {

    public AddExamController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/Instructor/createNewExam.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectCode = request.getParameter("code");
//        System.out.println(subjectCode);
        String title = request.getParameter("title");
        String description = request.getParameter("exam_description");
//        System.out.println(title + " " + description);
        Exam exam = new Exam(subjectCode, title, description);
        int examID = ExamMapper.insert(exam);
        response.sendRedirect("/Instructor/addExamQuestions.jsp");
    }

}