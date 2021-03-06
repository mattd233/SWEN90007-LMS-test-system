package main.java.controller.auth;

import main.java.db.mapper.UserMapper;
import main.java.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final transient Logger log = LoggerFactory.getLogger(LoginController.class);

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
        String path = getServletContext().getRealPath("/WEB-INF/shiro.ini");
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(path);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        // get the currently executing user:
        Subject currentUser = SecurityUtils.getSubject();
//        User user = UserMapper.getUserWithUsernamePassword(username, password);

        // let's login the current user so we can check against roles and permissions:
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username of " + token.getPrincipal());
                PrintWriter writer = response.getWriter();
                writer.println("Invalid username or password");
            } catch (IncorrectCredentialsException ice) {
                log.info(("Password for account " + token.getPrincipal() + " was incorrect!"));
                PrintWriter writer = response.getWriter();
                writer.println("Invalid username or password");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
                ae.printStackTrace();
            }
        }

        User user = UserMapper.getUserWithUsername(username);
        HttpSession session = request.getSession();
        session.setAttribute("user_id", user.getUserID());

        // check the roles
        if (currentUser.hasRole("ADMIN")) {
            response.sendRedirect("Admin/subjects.jsp");
            log.info("User " + currentUser.getPrincipal() + " logged in as ADMIN");
        } else if (currentUser.hasRole("INSTRUCTOR")) {
            response.sendRedirect("Instructor/instructorSubjects.jsp");
            log.info("User " + currentUser.getPrincipal() + " logged in as INSTRUCTOR");
        } else if (currentUser.hasRole("STUDENT")) {
            response.sendRedirect("Student/studentHomePage.jsp");
            log.info("User " + currentUser.getPrincipal() + " logged in as STUDENT");
        }

    }
}
