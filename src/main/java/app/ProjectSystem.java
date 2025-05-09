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

    public Project createProject(
        String name,
        LocalDate start,
        LocalDate end,
        double budgetedTime
    ) {

        // Preconditions
        assert name != null && !name.trim().isEmpty() : "Project name cannot be null or empty";
        assert start != null : "Start date cannot be null";
        assert end != null : "End date cannot be null";
        assert !start.isAfter(end) : "Start date must be before or equal to end date";
        assert budgetedTime > 0 : "Budgeted time must be a positive number";
        //assert getProjectByName(name) == null : "Project with the same name already exists";

        if (getProjectByName(name) != null) {
            throw new IllegalArgumentException(
                "Project already exists. New project has not been created."
            );
        }

        String id = generateProjectID();
        Project project = new Project(name, id, start, end, budgetedTime);
        projects.add(project);

        // Postconditions
        assert projects.contains(project) : "Project was not added successfully";
        assert project.getId() != null && !project.getId().trim().isEmpty() : "Project ID is invalid";


        return project;
    }

    public String generateProjectID() {
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


     // Adds an employee to a project only.
    public void addEmployee(String employeeId, String projectName) {
        // Precondition asserts
        assert employeeId != null && !employeeId.isEmpty() : "Precondition failed: employeeId must be non-null and non-empty";
        assert projectName != null && !projectName.isEmpty() : "Precondition failed: projectName must be non-null and non-empty";
        assert isRegistered(employeeId) : "Precondition failed: employee must be registered";

        Project project = getProjectByName(projectName);
        assert project != null : "Precondition failed: project must exist";

        Employee employee = getEmployeeById(employeeId);
        int initialEmployeeCount = project.getEmployees().size();

        if (!project.getEmployees().contains(employee)) {
            project.addEmployee(employee);
        }

        // Postcondition asserts
        assert project.getEmployees().contains(employee) : "Postcondition failed: employee must be added to project";
        assert project.getEmployees().size() >= initialEmployeeCount : "Postcondition failed: employee count must stay same or increase";
    }


    // Adds an employee to a project and an activity.
    public void addEmployee(String employeeId, String projectName, String activityName) {
        // Precondition asserts
        assert employeeId != null && !employeeId.isEmpty() : "Precondition failed: employeeId must be non-null and non-empty";
        assert projectName != null && !projectName.isEmpty() : "Precondition failed: projectName must be non-null and non-empty";
        assert activityName != null && !activityName.isEmpty() : "Precondition failed: activityName must be non-null and non-empty";

        Project project = getProjectByName(projectName);
        assert project != null : "Precondition failed: project must exist";

        Activity activity = project.getActivityByName(activityName);
        assert activity != null : "Precondition failed: activity must exist";

        Employee employee = getEmployeeById(employeeId);


        activity.assignEmployee(employee);

        // Postcondition asserts
        assert activity.isEmployeeAssigned(employee) : "Postcondition failed: employee must be assigned to activity";

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

}
