package main.java.controller.instructor;

import main.java.db.mapper.ExamMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/instructor/update_exam_status")
public class ExamStatusController extends HttpServlet {

    public ExamStatusController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            int examID = Integer.valueOf(request.getParameter("exam_id"));
            if (action.equals("publish")) {
                if (!ExamMapper.publishExam(examID)) {
                    System.err.println("Error in ExamStatusController: Cannot publish the exam");
                    showErrorPage(request, response);
                }
            } else if (action.equals("close")) {
                if (!ExamMapper.closeExam(examID)) {
                    System.err.println("Error in ExamStatusController: Cannot close the exam");
                    showErrorPage(request, response);
                }
            } else if (action.equals("delete")) {
                if (!ExamMapper.deleteExam(examID)) {
                    // TODO: error handling
                    System.err.println("Error in ExamStatusController: Cannot delete the exam");
                    showErrorPage(request, response);
                }
            }
            String subjectCode = request.getParameter("subject_code");
            response.sendRedirect("/Instructor/instructorExams.jsp?subject_code=" + subjectCode);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorPage(request, response);
        }
    }

    private void showErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/errorPage.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }
}