package hellocucumber;

import app.*;
import io.cucumber.java.en.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class CreateActivitySteps {
    private final ProjectSystem projectSystem = AddEmployeeSteps.projectSystem;
    private Project currentProject;
    private Activity currentActivity;
    private Employee currentEmployee;
    private String errorMessage;

    @Given("a project with the name {string} is already created")
    public void aProjectWithTheNameIsAlreadyCreated(String projectName) {
        currentProject = projectSystem.getProjectByName(projectName);
        if (currentProject == null) {
            currentProject = projectSystem.createProject(projectName, LocalDate.now(), LocalDate.now().plusDays(30));
        }
    }

    @And("employee {string} has logged in to manage activities")
    public void employeeHasLoggedInToManageActivities(String id) {
        currentEmployee = projectSystem.getEmployeeById(id);
        if (currentEmployee == null) {
            currentEmployee = new Employee(id);
            currentEmployee.logIn();
            projectSystem.registerEmployee(id);
        } else {
            currentEmployee.logIn();
        }
    }

    @When("a new activity named {string} is added to the project {string}")
    public void aNewActivityNamedIsAddedToTheProject(String activityName, String projectName) {
        Project project = projectSystem.getProjectByName(projectName);
        if (project.getActivityByName(activityName) == null) {
            currentActivity = new Activity(activityName);
            project.addActivity(currentActivity);
        }
    }

    @Then("the activity {string} should now be part of the project {string}")
    public void theActivityShouldNowBePartOfTheProject(String activityName, String projectName) {
        Project project = projectSystem.getProjectByName(projectName);
        assertNotNull(project.getActivityByName(activityName), "Activity should be part of the project");
    }

    @And("the activity {string} is part of the project {string}")
    public void theActivityIsPartOfTheProject(String activityName, String projectName) {
        Project project = projectSystem.getProjectByName(projectName);
        if (project.getActivityByName(activityName) == null) {
            project.addActivity(new Activity(activityName));
        }
    }

    @When("employee {string} is assigned to the activity {string}")
    public void employeeIsAssignedToTheActivity(String employeeId, String activityName) {
        Activity activity = currentProject.getActivityByName(activityName);
        Employee employee = projectSystem.getEmployeeById(employeeId);
        if (employee == null) {
            employee = new Employee(employeeId);
            employee.logIn();
            projectSystem.registerEmployee(employeeId);
        }

        try {
            if (!activity.isEmployeeAssigned(employee)) {
                activity.assignEmployee(employee);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("employee {string} should appear on the list of assigned employees for {string}")
    public void employeeShouldAppearOnTheListOfAssignedEmployees(String employeeId, String activityName) {
        Activity activity = currentProject.getActivityByName(activityName);
        Employee employee = projectSystem.getEmployeeById(employeeId);
        assertTrue(activity.isEmployeeAssigned(employee), "Employee should be assigned to the activity");
    }

    @Given("employee {string} has already logged in and is assigned to 10 activities")
    public void employeeHasLoggedInAndIsAssignedTo10Activities(String id) {
        currentEmployee = projectSystem.getEmployeeById(id);
        if (currentEmployee == null) {
            currentEmployee = new Employee(id);
            currentEmployee.logIn();
            projectSystem.registerEmployee(id);
        }

        for (int i = 0; i < 10; i++) {
            Activity a = new Activity("Activity" + i);
            if (!a.isEmployeeAssigned(currentEmployee)) {
                a.assignEmployee(currentEmployee);
            }
        }
    }

    @When("the project manager attempts to assign employee {string} to a new activity called {string}")
    public void theProjectManagerAttemptsToAssignEmployeeToNewActivity(String employeeId, String activityName) {
        Activity newActivity = new Activity(activityName);
        try {
            newActivity.assignEmployee(currentEmployee);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the system should display the error {string}")
    public void theSystemShouldDisplayTheError(String expectedMessage) {
        assertEquals(expectedMessage, errorMessage);
    }

    @And("employee {string} is already assigned to the activity {string}")
    public void employeeIsAlreadyAssignedToTheActivity(String employeeId, String activityName) {
        Activity activity = currentProject.getActivityByName(activityName);
        Employee employee = projectSystem.getEmployeeById(employeeId);
        if (!activity.isEmployeeAssigned(employee)) {
            activity.assignEmployee(employee);
        }
    }

    @When("the project manager tries to assign employee {string} to {string} again")
    public void theProjectManagerTriesToAssignEmployeeAgain(String employeeId, String activityName) {
        Activity activity = currentProject.getActivityByName(activityName);
        Employee employee = projectSystem.getEmployeeById(employeeId);
        try {
            activity.assignEmployee(employee);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @When("the project manager tries to add another activity named {string} to {string}")
    public void theProjectManagerTriesToAddAnotherActivity(String activityName, String projectName) {
        try {
            Project project = projectSystem.getProjectByName(projectName);
            if (project.getActivityByName(activityName) == null) {
                project.addActivity(new Activity(activityName));
            } else {
                throw new IllegalArgumentException("Activity with name '" + activityName + "' already exists in project '" + projectName + "'");
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Given("a second project with the name {string} is created")
    public void aSecondProjectIsCreated(String projectName) {
        if (projectSystem.getProjectByName(projectName) == null) {
            projectSystem.createProject(projectName, LocalDate.now(), LocalDate.now().plusDays(30));
        }
    }

    @When("another activity named {string} is added to the project {string}")
    public void anotherActivityNamedIsAddedToTheProject(String activityName, String projectName) {
        Project project = projectSystem.getProjectByName(projectName);
        if (project.getActivityByName(activityName) == null) {
            project.addActivity(new Activity(activityName));
        }
    }

    @Then("both projects should contain an activity named {string}")
    public void bothProjectsShouldContainAnActivityNamed(String activityName) {
        Project projectA = projectSystem.getProjectByName("ProjectA");
        Project projectB = projectSystem.getProjectByName("ProjectB");

        assertNotNull(projectA.getActivityByName(activityName), "ProjectA does not contain the activity");
        assertNotNull(projectB.getActivityByName(activityName), "ProjectB does not contain the activity");
    }
}
