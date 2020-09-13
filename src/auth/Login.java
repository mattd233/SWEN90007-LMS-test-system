package auth;

import db.DBConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class Login extends HttpServlet {

    private static final String findPassWithUsername = "SELECT password FROM students WHERE username = ";

    // Display welcome page if the user enters the correct password and username
    // otherwise, go back to the original page
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String view = "/login.jsp";
        ServletContext servletContext = getServletContext();
        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);

//        String view = "/subjects.jsp";
//        ServletContext servletContext = getServletContext();
//        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
//        requestDispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get username and password from the form
        String username = request.getParameter("userName");
        String password = request.getParameter("passWord");
        PrintWriter writer = response.getWriter();
        try {
            // get the password for the entered username
            Connection connection = new db.DBConnection().connect();
            PreparedStatement stmt = connection.prepareStatement(findPassWithUsername + "'" +  username + "'");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                String returnedPassword = rs.getString("password");
                if (password.equals(returnedPassword)) {
                    writer.println("Logged in with "+username+ " " +password);
                } else {
                    writer.println("Invalid password");
                }
            } else {
                writer.println("Invalid username");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
