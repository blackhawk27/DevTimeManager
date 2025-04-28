package app;

import java.time.LocalDate;

public class Employee {
    public boolean loggedIn;
    private final String id;
    private String tempProjectName;
    private LocalDate tempStartDate;
    private LocalDate tempEndDate;
    private int assignedActivitiesCount = 0;

    public Employee(String id) {
        this.id = id;
        this.loggedIn = false;
    }

    public void logIn(){
        this.loggedIn = true;
    }

    public void logOut(){
        this.loggedIn = false;

    }
    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    public String getId(){
        return this.id;
    }

    public void inputProjectName(String name) {
        tempProjectName = name;
    }

    public void inputStartDate(LocalDate date) {
        tempStartDate = date;
    }

    public void inputEndDate(LocalDate date) {
        tempEndDate = date;
    }

    public Project createProject(ProjectSystem system) {
        if (tempProjectName == null || tempProjectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name missing. Project has not been created.");
        }
        return system.createProject(tempProjectName, tempStartDate, tempEndDate);
    }

    public boolean canTakeMoreActivities() {
        return assignedActivitiesCount < 10;
    }

    public void addToActivity() {
        assignedActivitiesCount++;
    }
}
