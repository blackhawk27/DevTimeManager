package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectSystem {
    private int projectCounter = 1;
    private List<Project> projects = new ArrayList<>();


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
}


