package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectSystem {
    private int projectCounter = 1;
    private List<Project> projects = new ArrayList<>();
    private Map<String, Employee> employees = new HashMap<>();


    public ProjectSystem() {
        // initialiser dine lister over projekter, medarbejdere osv.
    }


    public Project createProject(String name, LocalDate start, LocalDate end) {
        String id = generateProjectID();
        Project project = new Project(name, id, start, end);
        projects.add(project);
        return project;
    }

    private String generateProjectID() {
        String year = "25"; // 2025
        String id = year + String.format("%03d", projectCounter++);
        return id;
    }

    public Project getProjectByName(String name) {
        for (Project project : projects) {
            if (project.getName().equals(name)) {
                return project;
            }
        }
        return null;
    }

    public boolean isRegistered(String id) {
        return employees.containsKey(id);
    }

    public void registerEmployee(String id){
        employees.put(id ,new Employee(id));
    }

    public Employee getEmployeeById(String id) {
        return employees.get(id);
    }

    public void removeProjectByName(String projectName) {
        projects.removeIf(project -> project.getName().equals(projectName));
    }
}


