package main.java.controller.admin;

import main.java.db.mapper.SubjectMapper;
import main.java.db.mapper.UserMapper;
import main.java.db.mapper.UserSubjectMapper;
import main.java.domain.Instructor;
import main.java.domain.Subject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/subjects")
public class AddSubjectController extends HttpServlet {

    public AddSubjectController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // handles adding coordinator
        String target = "/Admin/subjects.jsp";
        String instructorID = request.getParameter("instructor_id");
        String subjectCode = request.getParameter("subject_code");
        if (instructorID != null && subjectCode !=null) {
            try {
                int id = Integer.parseInt(instructorID);
                Instructor instructor = UserMapper.findInstructorWithID(id);
                assert instructor != null;
                UserSubjectMapper.insert(id, subjectCode);
            } catch (Exception e) {
                target = "errorPage.jsp";
                e.printStackTrace();
            }
        }
        response.sendRedirect(target);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectCode = request.getParameter("code");
        String name = request.getParameter("name");
        String instructorID = request.getParameter("instructor_id");
        Subject subject = new Subject(subjectCode, name);
        Instructor instructor = UserMapper.findInstructorWithID(Integer.parseInt(instructorID));
        if (instructor==null) {
            response.getWriter().println("Invalid instructor ID");
        }
        subject.addInstructor(instructor.getStaffID(), instructor.getName());
        SubjectMapper.insert(subject);
        String view = "/Admin/subjects.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }
}
