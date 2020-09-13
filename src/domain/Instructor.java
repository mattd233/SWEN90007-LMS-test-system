package domain;

public class Instructor {
    private String coordinatorName;

    public Instructor(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public void setName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getName() {
        return coordinatorName;
    }
}
