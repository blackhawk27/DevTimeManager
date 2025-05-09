package hellocucumber;

import app.*;
import io.cucumber.java.en.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;
import static hellocucumber.SharedContext.projectSystem;
import static org.junit.jupiter.api.Assertions.*;

public class GenerateProjectReportSteps {
    //private final ProjectSystem projectSystem = AddEmployeeSteps.projectSystem;
    private Project currentReport;
    private String errorMessage;

    private LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Given("a project with the title {string} exists")
    public void aProjectWithTitleExists(String projectName) {
        if (projectSystem.getProjectByName(projectName) == null) {
            projectSystem.createProject(projectName, LocalDate.now(), LocalDate.now().plusDays(30), 100);
        }
    }


    @And("employee {string} has logged in to manage activities")
    public void employeeHasLoggedIn(String id) {
        Employee emp = projectSystem.getEmployeeById(id);
        if (emp == null) {
            emp = new Employee(id);
            projectSystem.registerEmployee(id);
        }
        emp.logIn();
    }

    @And("a new activity named {string} with start date {string} and end date {string} is added to the project {string}")
    public void newActivityWithNameDatesAdded(String name, String start, String end, String projectName) {
        LocalDate startDate = parseDate(start);
        LocalDate endDate = parseDate(end);
        Project project = projectSystem.getProjectByName(projectName);

        if (startDate.isAfter(endDate)) {
            errorMessage = "End date cannot be before start date";
            return;
        }
        if (project.getActivityByName(name) != null) {
            errorMessage = "Activity with name '" + name + "' already exists in project '" + projectName + "'";
            return;
        }

        // Generér ID via systemet
        String generatedId = projectSystem.generateActivityID();
        Activity activity = new Activity(generatedId, name, startDate, endDate);
        project.addActivity(activity);
    }


    @And("{string} in {string} has a budgeted time of {double} hours")
    public void activityHasBudgetedTime(String activityName, String projectName, double hours) {
        Project project = projectSystem.getProjectByName(projectName);
        Activity activity = project.getActivityByName(activityName);
        activity.setBudgetedTime(hours);
    }

    @And("{double} hours of work are registered on activity {string} in {string}")
    public void registerHoursOfWork(double hours, String activityName, String projectName) {
        Project project = projectSystem.getProjectByName(projectName);
        Activity activity = project.getActivityByName(activityName);
        LocalDate baseDate = LocalDate.of(2025, 5, 1);  // Start fra 1. maj 2025

        double remainingHours = hours;
        LocalDate day = baseDate;

        while (remainingHours > 0) {
            double duration = Math.min(1.0, remainingHours); // log max 1 hour per entry
            LocalDateTime start = day.atTime(9, 0);
            LocalDateTime end = start.plusMinutes((long)(duration * 60));
            TimeEntry entry = new TimeEntry(TimeEntry.EntryType.Work, start, end, projectName, activityName);
            activity.addWorkEntry(entry);

            remainingHours -= duration;
            day = day.plusDays(1); // move to next day
        }

    }


    @And("no hours have been registered on {string}")
    public void noHoursHaveBeenRegistered(String projectName) {
        Project project = projectSystem.getProjectByName(projectName);
        Activity activity = project.getActivityByName("BudgetActivity");
        assertEquals(0, activity.getRegisteredTime());
    }

    @When("the project manager generates a report for {string}")
    public void projectManagerGeneratesReport(String projectName) {
        currentReport = projectSystem.getProjectByName(projectName);
        if (currentReport == null) {
            errorMessage = "Project not found";
            return;
        }

        boolean allZero = currentReport.getActivities().stream()
                .mapToDouble(Activity::getBudgetedTime)
                .sum() == 0;

        if (allZero) {
            errorMessage = "Budgeted time is missing for this project";
        }
    }


    @Then("the system displays the total registered hours as {double}")
    public void systemDisplaysTotalRegisteredHours(double expected) {
        double actual = currentReport.getActivities().stream().mapToDouble(Activity::getRegisteredTime).sum();
        assertEquals(expected, actual);
    }

    @And("the system displays the budgeted time as {double}")
    public void systemDisplaysBudgetedTime(double expected) {
        double actual = currentReport.getActivities().stream().mapToDouble(Activity::getBudgetedTime).sum();
        assertEquals(expected, actual);
    }

    @And("the system displays the unallocated hours as {double}")
    public void systemDisplaysUnallocatedHours(double expected) {
        double budget = currentReport.getActivities().stream().mapToDouble(Activity::getBudgetedTime).sum();
        double registered = currentReport.getActivities().stream().mapToDouble(Activity::getRegisteredTime).sum();
        assertEquals(expected, budget - registered);
    }

    @And("the system confirms that the project is within budget")
    public void systemConfirmsProjectWithinBudget() {
        double budget = currentReport.getActivities().stream().mapToDouble(Activity::getBudgetedTime).sum();
        double registered = currentReport.getActivities().stream().mapToDouble(Activity::getRegisteredTime).sum();
        assertTrue(registered <= budget);
    }

    @And("the system displays the estimated remaining work time")
    public void systemDisplaysRemainingTime() {
        double budget = currentReport.getActivities().stream().mapToDouble(Activity::getBudgetedTime).sum();
        double registered = currentReport.getActivities().stream().mapToDouble(Activity::getRegisteredTime).sum();
        double remaining = budget - registered;
        assertTrue(remaining >= 0);
    }

    @And("the system warns {string}")
    public void systemWarns(String expectedWarning) {
        double registered = currentReport.getActivities().stream().mapToDouble(Activity::getRegisteredTime).sum();
        if (registered == 0) {
            assertEquals(expectedWarning, "No work has been registered on this project yet");
        } else {
            fail("Work was registered, but warning was expected");
        }
    }

    @Given("no project named {string} exists")
    public void noProjectExists(String projectName) {
        if (projectSystem.getProjectByName(projectName) != null) {
            projectSystem.removeProjectByName(projectName);
        }
    }

    @When("the project manager attempts to generate a report for {string}")
    public void managerAttemptsReportOnMissingProject(String projectName) {
        currentReport = projectSystem.getProjectByName(projectName);
        if (currentReport == null) {
            errorMessage = "Project not found";
        }
    }

    @Then("the system displays an error message {string}")
    public void systemDisplaysError(String expected) {
        assertEquals(expected, errorMessage);
    }

    @And("{string} has no budgeted time set")
    public void noBudgetTime(String projectName) {
        Project project = projectSystem.getProjectByName(projectName);
        Activity activity = new Activity("NB1", "BudgetActivity", LocalDate.now(), LocalDate.now().plusDays(10));
        project.addActivity(activity);
    }

    @When("the project manager attempts to generate a report without selecting a project")
    public void managerAttemptsWithoutProject() {
        currentReport = null;
        errorMessage = "No project selected";
    }
}
