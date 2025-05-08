package hellocucumber;

import app.ProjectSystem;
import io.cucumber.java.en.*;
import app.Employee;
import app.Project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class CreateProjectSteps {
    public static app.ProjectSystem projectSystem = new app.ProjectSystem();
    private Employee employee;
    private Project createdProject;
    private String errorMessage;
    private double budgetedTime;

    @Given("an employee with id {string} logs in to create project")
    public void anEmployeeLogsInToCreateProject(String id) {
        projectSystem = new ProjectSystem(); // Always start fresh for each scenario!
        employee = new Employee(id);
        employee.logIn();
        assertTrue(employee.isLoggedIn());
    }

    @Given("an employee with id {string} is not logged in")
    public void anEmployeeWithIdIsNotLoggedIn(String id) {
        projectSystem = new ProjectSystem();
        employee = new Employee(id);
        assertFalse(employee.isLoggedIn());
    }

    @When("the employee inputs name {string}")
    public void theEmployeeInputsName(String name) {
        employee.inputProjectName(name);
    }

    @And("the employee inputs and empty name")
    public void theEmployeeInputsAndEmptyName() {
        employee.inputProjectName("");
    }

    @When("the employee inputs start date {string}")
    public void theEmployeeInputsStartDate(String dateStr) {
        tryParseDate(dateStr, true);
    }

    @When("the employee inputs end date {string}")
    public void theEmployeeInputsEndDate(String dateStr) {
        tryParseDate(dateStr, false);
    }

    @When("the employee inputs budgeted time {double}")
    public void theEmployeeInputsBudgetedTime(double time) {
        employee.inputBudgetedTime(time);
    }

    private void tryParseDate(String dateStr, boolean isStartDate) {
        try {
            // Validate date format as dd/MM/yyyy
            if (!dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                if (isStartDate) {
                    employee.inputStartDate(null);
                } else {
                    employee.inputEndDate(null);
                }
                return;
            }

            // Manual date validation (split into day, month, year)
            String[] parts = dateStr.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            if (month < 1 || month > 12 || day < 1 || day > 31) {
                if (isStartDate) {
                    employee.inputStartDate(null);
                } else {
                    employee.inputEndDate(null);
                }
                return;
            }

            if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                if (isStartDate) {
                    employee.inputStartDate(null);
                } else {
                    employee.inputEndDate(null);
                }
                return;
            }

            if (month == 2) {
                boolean isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
                int maxDays = isLeapYear ? 29 : 28;

                if (day > maxDays) {
                    if (isStartDate) {
                        employee.inputStartDate(null);
                    } else {
                        employee.inputEndDate(null);
                    }
                    return;
                }
            }

            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (isStartDate) {
                employee.inputStartDate(date);
            } else {
                employee.inputEndDate(date);
            }
        } catch (Exception e) {
            if (isStartDate) {
                employee.inputStartDate(null);
            } else {
                employee.inputEndDate(null);
            }
        }
    }

    @And("the employee creates the project")
    public void theEmployeeCreatesTheProject() {
        try {
            createdProject = employee.createProject(projectSystem);
            errorMessage = null;
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
            createdProject = null;
        }
    }

    @Then("the system creates the project {string}")
    public void theSystemCreatesTheProject(String expectedName) {
        assertNotNull(createdProject, "Project should have been created but wasn't");
        assertEquals(expectedName, createdProject.getName());
    }

    @Then("the system returns a project ID {string}")
    public void theSystemReturnsAProjectID(String expectedId) {
        assertNotNull(createdProject, "Project should have been created but wasn't");
        assertEquals(expectedId, createdProject.getId());
    }

    @Then("the system outputs the error message {string}")
    public void theSystemOutputsTheErrorMessage(String expectedErrorMessage) {
        assertNotNull(errorMessage, "Expected an error message but none was thrown");
        assertEquals(expectedErrorMessage, errorMessage);
    }

    @And("the project is not created")
    public void theProjectIsNotCreated() {
        assertNull(createdProject, "Project should not have been created");
    }
}
