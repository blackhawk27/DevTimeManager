package hellocucumber;

import app.Employee;
import app.Project;
import app.ProjectManager;
import app.ProjectSystem;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AddProjectManagerToProjectSteps {
    private Project project;
    private Employee employee;
    private ProjectManager projectManager;
    private String errorMessage;
    private Map<String, Employee> employees = new HashMap<>();
    private ProjectSystem projectSystem = new ProjectSystem();

    @Given("a project {string} exists with no manager")
    public void aProjectExists(String projectName) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(6);
        String projectId = "250001";  // Example project ID (you can generate this dynamically)
        project = new Project(projectName, projectId, startDate, endDate, 0.0);
    }

    @And("an employee with id {string} is assigned to {string}")
    public void anEmployeeWithIdIsAssignedTo(String employeeId, String projectName) {
        //employee = new Employee(employeeId);
        //project.addEmployee(employee);
        Employee employee = new Employee(employeeId);
        employees.put(employeeId, employee);
        project.addEmployee(employee);
    }

    @And("no Project Manager is assigned to {string}")
    public void noProjectManagerIsAssignedTo(String projectName) {
        project.setProjectManager(null);
    }

    @When("{string} assigns {string} as project manager to {string}")
    public void assignsAsProjectManagerTo(String employeeId, String managerId, String projectName) {
        //employee = new Employee(employeeId);
        //projectManager = new ProjectManager(managerId);  // Create project manager
        //project.assignProjectManager(employee, projectManager);  // Assumes a method in Project class for this
        Employee assigningEmployee = employees.get(employeeId);
        if (assigningEmployee == null) {
            throw new IllegalStateException("Employee " + employeeId + " not found");
        }
        projectManager = new ProjectManager(managerId);
        project.assignProjectManager(assigningEmployee, projectManager);
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

    @Given("a project {string} exists with {string} as the project manager")
    public void aProjectExistsWithManager(String projectName, String managerId) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(6);
        String projectId = "0001";  // Example project ID
        project = new Project(projectName, projectId, startDate, endDate, 0.0);

        ProjectManager manager = new ProjectManager(managerId);
        project.setProjectManager(manager);
        manager.addProject(project);
    }

    @When("{string} tries to assign {string} as the project manager of {string}")
    public void triesToAssignAsProjectManagerOf(String assigningEmployeeId, String newManagerId, String projectName) {
        // Set up the assigning employee and the new project manager
        Employee assigningEmployee = new Employee(assigningEmployeeId);
        ProjectManager newManager = new ProjectManager(newManagerId);

        try {
            // Try assigning project manager
            project.assignProjectManager(assigningEmployee, newManager);
        } catch (IllegalStateException e) {
            errorMessage = e.getMessage();  // Catch the error message
        }
    }

    @Then("{string} will get an error message {string}")
    public void willGetAnErrorMessage(String assigningEmployeeId, String expectedMessage) {
        // Check if the error message is correct
        assertEquals(expectedMessage, errorMessage);
    }

    @And("an employee with id {string} is not assigned to {string}")
    public void anEmployeeWithIdIsNotAssignedTo(String employeeId, String projectName) {
        employee = new Employee(employeeId);
    }

    @And("{string} should now be of the ProjectManager class")
    public void shouldNowBeOfTheProjectManagerClass(String managerId) {
        assertTrue(project.getProjectManager() instanceof ProjectManager);  // Check the class type
    }

    @When("{string} tries to assign the unregistered {string} as the project manager of {string}")
    public void unregisteredAssignAsProjectManagerOf(String assigningEmployeeId, String newManagerId, String projectName) {
        Employee assigningEmployee = employees.get(assigningEmployeeId);
        if (assigningEmployee == null) {
            assigningEmployee = new Employee(assigningEmployeeId);
            employees.put(assigningEmployeeId, assigningEmployee);
        }

        Employee newManagerEmployee = employees.get(newManagerId);
        if (newManagerEmployee == null) {
            newManagerEmployee = new Employee(newManagerId);
            employees.put(newManagerId, newManagerEmployee);
        }

        ProjectManager newManager = new ProjectManager(newManagerEmployee.getId());

        try {
            project.assignProjectManager(assigningEmployee, newManager);
        } catch (IllegalStateException e) {
            errorMessage = e.getMessage();
        }
    }

}

