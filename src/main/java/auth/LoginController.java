package main.java.auth;

import main.java.db.DBConnection;
import main.java.db.mapper.UserMapper;
import main.java.domain.Admin;
import main.java.domain.Instructor;
import main.java.domain.Student;
import main.java.domain.User;

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

@WebServlet("/login")
public class LoginController extends HttpServlet {

    // Display welcome page if the user enters the correct password and username
    // otherwise, go back to the original page
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/login.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get username and password from the form
        String username = request.getParameter("userName");
        String password = request.getParameter("passWord");
        User user = UserMapper.getUserWithUsernamePassword(username, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user_id", user.getUserID());
            if (user instanceof Admin) {
                response.sendRedirect("index.jsp");
            } else if (user instanceof Instructor) {
                response.sendRedirect("Instructor/instructorSubjects.jsp");
            } else if (user instanceof Student){
                response.sendRedirect("Student/studentHomePage.jsp");
            }
        } else {
            PrintWriter writer = response.getWriter();
            writer.println("Invalid username or password");
        }
    }

}
