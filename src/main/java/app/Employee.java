package app;

import java.time.LocalDate;

public class Employee {
    public boolean loggedIn;
    private final String initials;
    private String tempProjectName;
    private LocalDate tempStartDate;
    private LocalDate tempEndDate;

    public Employee(String initials) {
        this.initials = initials;
        this.loggedIn = false;
    }

    public void logIn(){
        this.loggedIn = true;
    }

    public void logOut(){
        this.loggedIn = false;

    }
    public boolean isLoggedInStatus(){
        return this.loggedIn;
    }

    public String getInitials(){
        return this.initials;
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
        return system.createProject(tempProjectName, tempStartDate, tempEndDate);
    }
}
