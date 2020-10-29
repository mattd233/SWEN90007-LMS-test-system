package main.java.concurrency;

public class AppSession {
    private String user;
    private String id;

    public AppSession(String user, String id) {
        this.user = user;
        this.id = id;
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
