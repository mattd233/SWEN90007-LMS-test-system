package main.java.concurrency;

import main.java.domain.User;
import org.apache.shiro.SecurityUtils;

public class AppSession {
    public static final String INSTRUCTOR = "INSTRUCTOR";
    private String user;
    private String id;

    public AppSession(String user, String id) {
        this.user = user;
        this.id = id;
    }
    public static boolean hasRole(String role) {
        return SecurityUtils.getSubject().hasRole(role);
    }
    public static boolean isAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }
    public static void init(User user) {
        SecurityUtils.getSubject().getSession().setAttribute("user", user);
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
