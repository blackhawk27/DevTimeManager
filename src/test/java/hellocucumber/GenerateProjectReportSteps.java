package hellocucumber;

import app.Activity;
import app.Project;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GenerateProjectReportSteps {
    //private  Map<String, Project> projects = new HashMap<String, Project>();
    private Project currentReport;
    private String loggedInUserRole;
    private String errorMessage;

    @And("{string} has a budgeted time of {int} hours")
    public void hasABudgetedTimeOfHours(String projectName, int budgetedTime) {
        Project project = AddEmployeeSteps.projectSystem.getProjectByName(projectName);
        if (project != null) {
            Activity budgetActivity = new Activity("BudgetActivity");
            budgetActivity.setBudgetedTime(budgetedTime);
            project.addActivity(budgetActivity);
        } else {
            throw new IllegalStateException("Project does not exist: '" + projectName + "'");
        }


    }

    @And("the total registered work time on {string} is {int} hours")
    public void theTotalRegisteredWorkTimeOnIsHours(String projectName, int registeredTime) {
        Project project = AddEmployeeSteps.projectSystem.getProjectByName(projectName);
        if (project != null) {
            Activity activity = project.getActivityByName("BudgetActivity");
            if (activity != null) {
                activity = new Activity("BudgetActivity");
                project.addActivity(activity);
            }
            assert activity != null;
            activity.setRegisteredTime(registeredTime);
        } else {
            throw new IllegalStateException("Project does not exist: ");
        }

    }

    @And("the project manager is logged in")
    public void theProjectManagerIsLoggedIn() {
        //ingenting
    }

    @When("the project manager generates a report for {string}")
    public void theProjectManagerGeneratesAReportFor(String projectName) {
        currentReport = AddEmployeeSteps.projectSystem.getProjectByName(projectName);
        if (currentReport == null) {
            throw new IllegalStateException("Project does not exist: " + projectName);
        }
    }

    @Then("the system displays the total registered hours as {int}")
    public void theSystemDisplaysTheTotalRegisteredHoursAs(int arg0) {
        int totalRegistered = currentReport.getActivities().stream()
                .mapToInt(Activity::getRegisteredTime)
                .sum();
        assertEquals(arg0, totalRegistered, "Registered hours mismatch");
    }

    @And("the system displays the budgeted time as {int}")
    public void theSystemDisplaysTheBudgetedTimeAs(int arg0) {
        int totalBudget = currentReport.getActivities().stream()
                .mapToInt(Activity::getBudgetedTime)
                .sum();
        assertEquals(arg0, totalBudget, "Budgeted time mismatch");
    }

    @And("the system displays the unallocated hours as {int}")
    public void theSystemDisplaysTheUnallocatedHoursAs(int arg0) {
        int totalBudget = currentReport.getActivities().stream()
                .mapToInt(Activity::getBudgetedTime)
                .sum();
        int totalRegistered = currentReport.getActivities().stream()
                .mapToInt(Activity::getRegisteredTime)
                .sum();
        int unallocated = totalBudget - totalRegistered;
        assertEquals(arg0, unallocated, "Unallocated hours mismatch");
    }

    @And("the system confirms that the project is within budget")
    public void theSystemConfirmsThatTheProjectIsWithinBudget() {
        int totalBudget = currentReport.getActivities().stream()
                .mapToInt(Activity::getBudgetedTime)
                .sum();
        int totalRegistered = currentReport.getActivities().stream()
                .mapToInt(Activity::getRegisteredTime)
                .sum();
        assertTrue(totalRegistered <= totalBudget, "Project is over budget!");
    }

    @And("the system displays the estimated remaining work time")
    public void theSystemDisplaysTheEstimatedRemainingWorkTime() {
        int totalBudget = currentReport.getActivities().stream()
                .mapToInt(Activity::getBudgetedTime)
                .sum();
        int totalRegistered = currentReport.getActivities().stream()
                .mapToInt(Activity::getRegisteredTime)
                .sum();
        int remaining = totalBudget - totalRegistered;
        assertTrue(remaining >= 0, "Remaining work time is negative!");
    }

    @And("no hours have been registered on {string}")
    public void noHoursHaveBeenRegisteredOn(String projectName) {
        Project project = AddEmployeeSteps.projectSystem.getProjectByName(projectName);
        Activity activity = project.getActivityByName("BudgetActivity");
        if (activity == null) {
            throw new IllegalStateException("BudgetActivity findes ikke i projektet: '" + projectName + "'");
        }
        assertEquals(0, activity.getRegisteredTime(), "Expected no hours to be registered yet.");
    }

    @And("the system warns {string}")
    public void theSystemWarns(String expectedWarning) {
        int totalRegistered = currentReport.getActivities().stream()
                .mapToInt(Activity::getRegisteredTime)
                .sum();
        if (totalRegistered == 0) {
            String actualWarning = "No work has been registered on this project yet";
            assertEquals(actualWarning, expectedWarning, "Warning message mismatch");
        } else {
            throw new IllegalStateException("Expected a warning because no work was registered, but work was found.");
        }
    }

    @Given("no project named {string} exists")
    public void noProjectNamedExists(String projectName) {
        Project project = AddEmployeeSteps.projectSystem.getProjectByName(projectName);
        if (project != null) {
            AddEmployeeSteps.projectSystem.removeProjectByName(projectName);
        }
    }

    @When("the project manager attempts to generate a report for {string}")
    public void theProjectManagerAttemptsToGenerateAReportFor(String projectName) {
        currentReport = AddEmployeeSteps.projectSystem.getProjectByName(projectName);
        if (currentReport == null) {
            errorMessage = "Project not found";
        }
    }

    @Then("the system displays an error message {string}")
    public void theSystemDisplaysAnErrorMessage(String expectedMessage) {
        if (currentReport == null) {
            // Projektet findes ikke
            assertEquals(expectedMessage, errorMessage, "Error message mismatch (project not found case)");
        } else {
            // Projektet findes men måske uden budget
            int totalBudget = currentReport.getActivities().stream()
                    .mapToInt(Activity::getBudgetedTime)
                    .sum();
            if (totalBudget == 0) {
                errorMessage = "Budgeted time is missing for this project";
            } else {
                errorMessage = null;
            }
            assertEquals(expectedMessage, errorMessage, "Error message mismatch (budget missing case)");
        }
    }

    @And("{string} has no budgeted time set")
    public void hasNoBudgetedTimeSet(String projectName) {
        Project project = AddEmployeeSteps.projectSystem.getProjectByName(projectName);
        if (project != null) {
            Activity budgetActivity = new Activity("BudgetActivity");
            project.addActivity(budgetActivity);
        } else {
            throw new IllegalStateException("Project does not exist: " + projectName);
        }
    }

    @When("the project manager attempts to generate a report without selecting a project")
    public void theProjectManagerAttemptsToGenerateAReportWithoutSelectingAProject() {
        currentReport = null;
        errorMessage = "No project selected";
    }
}
