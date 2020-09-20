package auth;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class Login extends HttpServlet {

    private static final String findPassWithUsername = "SELECT * FROM users WHERE username = ?";

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
        PrintWriter writer = response.getWriter();
        try {
            // get the password for the entered username
            Connection connection = new db.DBConnection().connect();
            PreparedStatement stmt = connection.prepareStatement(findPassWithUsername);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                int userID = rs.getInt("user_id");
                String type = rs.getString("type");
                String returnedPassword = rs.getString("password");
                if (password.equals(returnedPassword)) {
                    String view = "/index.jsp";
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", userID);
                    if (type.equals("instructor")) {
                        view = "/Instructor/instructorSubjects.jsp";
                    }
                    // add student and admin here
                    ServletContext servletContext = getServletContext();
                    RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(view);
                    requestDispatcher.forward(request, response);
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
