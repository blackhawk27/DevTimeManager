package hellocucumber;

import app.ProjectSystem;
import io.cucumber.java.en.*;
import app.Employee;
import app.Project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class CreateProjectSteps {
    private Employee employee;
    private ProjectSystem projectSystem;
    private Project createdProject;
    private String errorMessage;

    @Given("an employee with initials {string} is logged in")
    public void anEmployeeWithNameIsLoggedIn(String arg0) {
        projectSystem = new ProjectSystem(); // Always start fresh for each scenario!
        employee = new Employee(arg0);
        employee.logIn();
        assertTrue(employee.isLoggedIn());
    }

    @When("the employee inputs name {string}")
    public void theEmployeeInputsName(String name) {
        employee.inputProjectName(name);
    }

    @When("the employee inputs start date {string}")
    public void theEmployeeInputsStartDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        employee.inputStartDate(date);
    }

    @When("the employee inputs end date {string}")
    public void theEmployeeInputsEndDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        employee.inputEndDate(date);
    }

    @Then("the system creates the project {string}")
    public void theSystemCreatesTheProject(String expectedName) {
        assertEquals(expectedName, createdProject.getName());
    }

    @Then("the system returns a project ID {string}")
    public void theSystemReturnsAProjectID(String expectedId) {
        assertEquals(expectedId, createdProject.getId());
    }

    @And("the employee inputs and empty name")
    public void theEmployeeInputsAndEmptyName() {
        employee.inputProjectName(""); // Actually input an empty string
    }

    @Then("the system outputs the error message {string}")
    public void theSystemOutputsTheErrorMessage(String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employee.createProject(projectSystem);
        });
        errorMessage = exception.getMessage();
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @And("the project is not created")
    public void theProjectIsNotCreated() {
        assertNull(createdProject);
    }

    @And("the employee creates the project")
    public void theEmployeeCreatesTheProject() {
        try {
            createdProject = employee.createProject(projectSystem);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
            createdProject = null;
        }
    }

}
