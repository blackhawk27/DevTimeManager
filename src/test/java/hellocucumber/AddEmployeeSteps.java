package hellocucumber;

import app.Activity;
import app.Employee;
import app.Project;
import app.ProjectSystem;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class AddEmployeeSteps {

    private Project project;
    private Activity activity;
    private Employee employee;
    private String errorMessage;

    public static final Map<String, Project> sharedProjects = new HashMap<>();
    public static final ProjectSystem projectSystem = new ProjectSystem();

    @Given("a project {string} exists")
    public void aProjectExists(String projectName) {
        projectSystem.createProject(
                projectName,
                LocalDate.now(),
                LocalDate.now().plusMonths(6)
        );
    }


    @And("an activity {string} exists in {string}")
    public void anActivityExistsIn(String activityName, String projectName) {
        //activity = new Activity(activityName);
        //project.addActivity(activity);
        Project project = projectSystem.getProjectByName(projectName);
        if (project != null) {
            activity = new Activity(activityName);  //
            project.addActivity(activity);
        } else {
            throw new IllegalArgumentException("Project " + projectName + " does not exist");
        }
    }

    @And("an employee with id {string} exists")
    public void anEmployeeExists(String id) {
        employee = new Employee(id);
    }

    @When("{string} is assigned to {string}")
    public void isAssignedTo(String id, String activityName) {
        try {
            activity.assignEmployee(employee);
        } catch (Exception e) {
            errorMessage = e.getMessage(); // save the error to verify later
        }
    }

    @Then("{string} should be listed in the employee list for {string}")
    public void shouldBeListedInTheEmployeeListFor(String id, String activityName) {
        assertTrue(activity.isEmployeeAssigned(employee));
    }

    @And("{string} is already assigned to {int} activities")
    public void isAlreadyAssignedToActivities(String id, int activityCount) {
        for (int i=0 ; i<activityCount ; i++) {
            Activity dummyActivity = new Activity("Dummy activity" + i);
            dummyActivity.assignEmployee(employee);
        }
    }

    @Then("the system should return an error message {string}")
    public void theSystemShouldReturnAnErrorMessage(String expectedMessage) {
        assertEquals(expectedMessage, errorMessage);
    }

    @When("{string} is assigned to {string} again")
    public void isAssignedToAgain(String id, String activityName) {
        try {
            activity.assignEmployee(employee);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }
}
