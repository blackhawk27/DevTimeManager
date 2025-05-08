package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectSystem {

    private int projectCounter = 1;
    private int activityCounter = 1; // fx i ProjectSystem

    private List<Project> projects = new ArrayList<>();
    private Map<String, Employee> employees = new HashMap<>();

    public ProjectSystem() {
        // initialiser dine lister over projekter, medarbejdere osv.
    }

    public Project createProject(
        String name,
        LocalDate start,
        LocalDate end,
        double budgetedTime
    ) {
        if (getProjectByName(name) != null) {
            throw new IllegalArgumentException(
                "Project already exists. New project has not been created."
            );
        }

        String id = generateProjectID();
        Project project = new Project(name, id, start, end, budgetedTime);
        projects.add(project);
        return project;
    }

    private String generateProjectID() {
        String year = (LocalDate.now().getYear() % 100) + "";
        String id = year + String.format("%03d", projectCounter++);
        return id;
    }

    public String generateActivityID() {
        return "A" + String.format("%03d", activityCounter++);
    }

    public Project getProjectByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Project name cannot be null or empty"
            );
        }

        for (Project project : projects) {
            if (project.getName().equalsIgnoreCase(name.trim())) {
                return project;
            }
        }
        return null;
    }

    /**
     * Adds an employee to a project only.
     */
    public void addEmployee(String employeeId, String projectName) {
        if (!isRegistered(employeeId)) {
            throw new IllegalArgumentException(
                "Employee with ID " + employeeId + " does not exist"
            );
        }

        Project project = getProjectByName(projectName);
        if (project == null) {
            throw new IllegalArgumentException(
                "Project with ID " + projectName + " does not exist"
            );
        }

        Employee employee = getEmployeeById(employeeId);

        // Undgå at tilføje samme medarbejder flere gange
        if (!project.getEmployees().contains(employee)) {
            project.addEmployee(employee); // Projektet håndterer selv sin liste
        }
    }

    /**
     * Adds an employee to a project and an activity.
     */
    public void addEmployee(
        String employeeId,
        String projectName,
        String activityName
    ) {
        // Først sikre at medarbejderen er på projektet – uden duplikat
        addEmployee(employeeId, projectName);

        Project project = getProjectByName(projectName);
        Activity activity = project.getActivityByName(activityName);
        if (activity == null) {
            throw new IllegalArgumentException(
                "Activity " +
                activityName +
                " not found in project " +
                projectName
            );
        }

        Employee employee = getEmployeeById(employeeId);

        // Undgå at tilføje medarbejderen flere gange til samme aktivitet
        if (!activity.isEmployeeAssigned(employee)) {
            activity.assignEmployee(employee); // Aktiviteten håndterer sine egne regler (fx max 10 aktiviteter)
        }
    }

    public boolean isRegistered(String id) {
        return employees.containsKey(id);
    }

    public void registerEmployee(String id) {
        employees.put(id, new Employee(id));
    }

    public Employee getEmployeeById(String id) {
        return employees.get(id);
    }

    public void removeProjectByName(String projectName) {
        projects.removeIf(project -> project.getName().equals(projectName));
    }

    public Project getProjectById(String id) {
        for (Project project : projects) {
            if (project.getId().equals(id)) {
                return project;
            }
        }
        return null;
    }
}
