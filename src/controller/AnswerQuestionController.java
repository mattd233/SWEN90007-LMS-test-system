package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description:
 */


@WebServlet("/Student/studentAnswerQuestions")
public class AnswerQuestionController extends HttpServlet {

    public AnswerQuestionController(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("processing loading answered questions");
        String examID = request.getParameter("examID");
        String studentID = request.getParameter("studentID");
        if (examID != null && studentID != null) {
            String view = "/Student/studentAnswerQuestions.jsp";
            ServletContext servletContext = getServletContext();
            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
            requestDispatcher.forward(request, response);
        } else {
            System.out.println("Error in AnswerQuestion doGet");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("processing recording answered questions");

        int index = Integer.parseInt(request.getParameter("index"));
        System.out.println("index: " + index);
        String answer = request.getParameter("answer");
        String exam_id = request.getParameter("exam_id");
        System.out.println("answer: " + answer);
        System.out.println("exam_id: " + exam_id);
        HttpSession session = request.getSession();
        String key = exam_id + "_" + index;
        session.setAttribute(key,answer);

        PrintWriter writer = response.getWriter();
        writer.write("success");
        writer.flush();

    }

}
