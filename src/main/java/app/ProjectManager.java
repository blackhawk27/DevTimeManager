package app;

import java.util.ArrayList;
import java.util.List;

public class ProjectManager extends Employee {
    private List<Project> managedProjects;

    public ProjectManager(String id) {
        super(id);
        this.managedProjects = new ArrayList<>();  // Initialize the list
    }

    public List<Project> getManagedProjects() {
        return this.managedProjects;
    }

    public void addProject(Project project) {
        this.managedProjects.add(project);
    }
}


