package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Employee> employees = new ArrayList<>();
    private final List<Activity> activities = new ArrayList<>();
    private ProjectManager projectManager;

    public Project(String name, String id, LocalDate start, LocalDate end) {
        this.name = name;
        this.id = id;
        this.startDate = start;
        this.endDate = end;
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
        activities.add(activity);
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
        return new ArrayList<>(activities); // Return a copy to protect internal list
    }

    public void setProjectManager(ProjectManager manager) {
        this.projectManager = manager;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void assignProjectManager(Employee assigningEmployee, ProjectManager newManager) {
        // If the project already has a manager, only the current manager can assign a new one
        if (this.projectManager != null) {
            if (this.projectManager.getId().equals(assigningEmployee.getId())) {
                setProjectManager(newManager);  // Assign the new manager
                // Add this project to the new manager's list of projects
                newManager.addProject(this);
            } else {
                throw new IllegalStateException(assigningEmployee.getId() + " is not the project manager and cannot assign one.");
            }
        } else {
            // If no project manager is assigned yet, anyone can assign one
            setProjectManager(newManager);
            // Add this project to the new manager's list of projects
            newManager.addProject(this);
        }
    }
}
