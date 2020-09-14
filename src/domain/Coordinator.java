package domain;

public class Coordinator {
    private int staffId;
    private String name;
    private String username;
    private String password;

    public Coordinator(int staffId, String name, String username, String password) {
        this.staffId = staffId;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




}
