package hellocucumber;

import app.Employee;
import app.Project;
import app.ProjectManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AddProjectManagerToProjectSteps {
    private Project project;
    private Employee employee;
    private ProjectManager projectManager;

    @Given("a project {string} exists with no manager")
    public void aProjectExists(String projectName) {
        LocalDate startDate = LocalDate.now();  // For example, using today's date
        LocalDate endDate = LocalDate.now().plusMonths(6);  // For example, 6 months from now
        String projectId = "0001";  // Example project ID (you can generate this dynamically)
        project = new Project(projectName, projectId, startDate, endDate);
    }

    @And("an employee with id {string} is assigned to {string}")
    public void anEmployeeWithIdIsAssignedTo(String employeeId, String projectName) {
        employee = new Employee(employeeId);
        project.addEmployee(employee);  // Assumes Project has an addEmployee method
    }

    @And("no Project Manager is assigned to {string}")
    public void noProjectManagerIsAssignedTo(String projectName) {
        project.setProjectManager(null);  // Assumes Project has setProjectManager method
    }

    @When("{string} assigns {string} as project manager to {string}")
    public void assignsAsProjectManagerTo(String employeeId, String managerId, String projectName) {
        employee = new Employee(employeeId);
        projectManager = new ProjectManager(managerId);  // Create project manager
        project.assignProjectManager(employee, projectManager);  // Assumes a method in Project class for this
    }


    @Then("{string} should be able to see {string} in their list of managed projects")
    public void shouldBeAbleToSeeInTheirListOfManagedProjects(String managerId, String projectName) {
        // Get the project manager assigned to the project
        ProjectManager manager = project.getProjectManager();  // Use the manager assigned to the project

        // Check if the manager can see the project in their list of managed projects
        boolean isManaged = manager.getManagedProjects().stream()
                .anyMatch(project -> project.getName().equals(projectName));

        assertTrue(isManaged);  // Assert that the project is indeed in the manager's list
    }
    @And("{string} should be the project manager of {string}")
    public void shouldBeTheProjectManagerOf(String managerId, String projectName) {
        assertEquals(managerId, project.getProjectManager().getId());  // Check the project manager ID
    }

    @And("{string} should now be of the ProjectManager class")
    public void shouldNowBeOfTheProjectManagerClass(String managerId) {
        assertTrue(project.getProjectManager() instanceof ProjectManager);  // Check the class type
    }
}
