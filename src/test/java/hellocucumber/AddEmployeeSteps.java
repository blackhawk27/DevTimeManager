package hellocucumber;

import app.*;
import io.cucumber.java.en.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import static hellocucumber.SharedContext.projectSystem;


import static org.junit.jupiter.api.Assertions.*;

public class AddEmployeeSteps {
    //public static final ProjectSystem projectSystem = new ProjectSystem();
    private Project project;
    private Activity activity;
    private Employee employee;
    private String errorMessage;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final Map<String, Project> sharedProjects = new HashMap<>();


    @Given("a project {string} exists")
    public void aProjectExists(String projectName) {
        project = projectSystem.getProjectByName(projectName);
        if (project == null) {
            project = projectSystem.createProject(projectName, LocalDate.now(), LocalDate.now().plusDays(30), 100);
        }
    }

    @And("an activity with name {string}, start date {string} and end date {string} exists in {string}")
    public void anActivityExists(String name, String startDate, String endDate, String projectName) {
        LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
        LocalDate end = LocalDate.parse(endDate, DATE_FORMAT);
        Project targetProject = projectSystem.getProjectByName(projectName);
        String generatedId = projectSystem.generateActivityID();
        activity = new Activity(generatedId, name, start, end);
        try {
            targetProject.addActivity(activity);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }


    @And("an employee with id {string} exists")
    public void anEmployeeExists(String id) {
        employee = projectSystem.getEmployeeById(id);
        if (employee == null) {
            employee = new Employee(id);
            employee.logIn();
            projectSystem.registerEmployee(id);
        }
    }

    @And("{string} is already assigned to 10 other activities")
    public void employeeIsAssignedTo10Activities(String empId) {
        Employee emp = projectSystem.getEmployeeById(empId);
        if (emp == null) {
            emp = new Employee(empId);
            emp.logIn();
            projectSystem.registerEmployee(empId);
        }
        for (int i = 0; i < 10; i++) {
            Activity dummy = new Activity("D" + i, "Dummy" + i, LocalDate.now(), LocalDate.now().plusDays(5));
            dummy.assignEmployee(emp);
        }
    }



    @And("{string} is already assigned to {string}")
    public void employeeIsAlreadyAssignedToActivity(String empId, String activityName) {
        Activity act = project.getActivityByName(activityName);
        Employee emp = projectSystem.getEmployeeById(empId);
        if (!act.isEmployeeAssigned(emp)) {
            act.assignEmployee(emp);
        }
    }


    @When("{string} is assigned to {string}")
    public void assignEmployeeToActivity(String empId, String activityName) {
        Activity act = project.getActivityByName(activityName);
        Employee emp = projectSystem.getEmployeeById(empId);
        try {
            act.assignEmployee(emp);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }


    @When("{string} is assigned to {string} again")
    public void assignToActivityAgain(String empId, String activityName) {
        assignEmployeeToActivity(empId, activityName);
    }

    @Then("{string} should be listed in the employee list for {string}")
    public void shouldBeListed(String empId, String activityName) {
        Activity act = project.getActivityByName(activityName);
        Employee emp = projectSystem.getEmployeeById(empId);
        assertTrue(act.isEmployeeAssigned(emp));
    }

    @Then("the system should return an error message {string}")
    public void shouldReturnError(String expectedMessage) {
        assertEquals(expectedMessage, errorMessage);
    }

}

