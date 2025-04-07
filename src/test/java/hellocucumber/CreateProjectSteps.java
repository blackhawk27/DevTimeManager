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
    private ProjectSystem projectSystem = new ProjectSystem();
    private Project createdProject;

    @Given("an employee with name {string} is logged in")
    public void anEmployeeWithNameIsLoggedIn(String arg0) {
        employee = new Employee(arg0);
        employee.logIn();
        assertTrue(employee.isLoggedInStatus());
    }

    @When("the employee creates a project")
    public void theEmployeeCreatesAProject() {
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
        createdProject = employee.createProject(projectSystem);
        assertEquals(expectedName, createdProject.getName());
    }

    @Then("the system returns a project ID {string}")
    public void theSystemReturnsAProjectID(String expectedId) {
        assertEquals(expectedId, createdProject.getId());
    }

}
