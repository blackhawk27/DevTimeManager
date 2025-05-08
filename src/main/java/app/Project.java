package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budgetedTime;
    private List<Employee> employees = new ArrayList<>();
    private final List<Activity> activities = new ArrayList<>();
    private ProjectManager projectManager;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Project(String name, String id, LocalDate start, LocalDate end, Double budgetedTime) {
        this.name = name;
        this.id = id;
        this.startDate = start;
        this.endDate = end;
        this.budgetedTime = budgetedTime;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void addActivity(Activity activity) {
        if (getActivityByName(activity.getName()) != null) {
            throw new IllegalArgumentException("Activity with name '" + activity.getName() + "' already exists in project '" + name + "'");
        }
        activities.add(activity);
    }


    public Double getBudgetedTime() {
        return budgetedTime;
    }


    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public Activity getActivityByName(String activityName) {
        for (Activity activity : activities) {
            if (activity.getName().equals(activityName)) {
                return activity;
            }
        }
        return null;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setProjectManager(ProjectManager manager) {
        this.projectManager = manager;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void assignProjectManager(Employee assigningEmployee, ProjectManager newManager) {
        if (this.projectManager != null) {
            throw new IllegalStateException("This project already has a project manager. You cannot assign a new one.");
        }
        if (!employees.contains(assigningEmployee)) {
            throw new IllegalStateException(assigningEmployee.getId() + " is not assigned to the project and cannot assign a manager.");
        }

        if (!employees.stream().anyMatch(e -> e.getId().equals(newManager.getId()))) {
            throw new IllegalStateException(newManager.getId() + " is not registered as an employee.");
        }
        // no manager yet → OK
        setProjectManager(newManager);
        newManager.addProject(this);
    }

}
