package hellocucumber;

import app.*;
import io.cucumber.java.en.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTimeSteps {

    private Employee employee;
    private ProjectSystem projectSystem;
    private Project project;
    private Activity activity;
    private String errorMessage;
    private int calculatedWorkHours;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Given("a project named {string} is prepared for time registration")
    public void prepareProjectForTimeRegistration(String projectName) {
        projectSystem = new ProjectSystem();
        project = projectSystem.getProjectByName(projectName);
        if (project == null) {
            project = projectSystem.createProject(projectName, LocalDate.now(), LocalDate.now().plusDays(30));
        }
    }


    @And("an activity with ID {string}, name {string}, start date {string} and end date {string} is prepared for time registration in project {string}")
    public void prepareActivityForTimeRegistration(String id, String name, String startDate, String endDate, String projectName) {
        LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
        LocalDate end = LocalDate.parse(endDate, DATE_FORMAT);
        activity = new Activity(id, name, start, end);
        project = projectSystem.getProjectByName(projectName);
        if (project.getActivityByName(name) == null) {
            project.addActivity(activity);
        }
    }


    @Given("an employee with id {string} is logged in")
    public void anEmployeeWithIdIsLoggedIn(String id) {
        employee = new Employee(id);
        employee.logIn();
        // Only register with projectSystem if we're in a project context
        if (projectSystem != null) {
            projectSystem.registerEmployee(id);
            project.addEmployee(employee);
        }
    }


    @And("employee {string} is assigned to the activity {string} for time registration")
    public void assignEmployeeForTimeRegistration(String id, String activityName) {
        if (project == null) {
            throw new IllegalStateException("Project must be initialized before assigning employees.");
        }

        if (employee == null) {
            throw new IllegalStateException("Employee must be initialized before being assigned.");
        }

        activity = project.getActivityByName(activityName);
        if (activity == null) {
            activity = new Activity("AX01", activityName, LocalDate.now(), LocalDate.now().plusDays(10));
            project.addActivity(activity);
        }

        if (!activity.isEmployeeAssigned(employee)) {
            activity.assignEmployee(employee);
        }
    }




    @When("the employee registers in the system that he started to work on {string} under {string} at {string} and stopped at {string}")
    public void theEmployeeRegistersInTheSystemThatHeStartedToWork(String activityName, String projectName, String startTime, String endTime) {
        ArrayList<String> date = new ArrayList<>();
        String today = LocalDate.now().format(DATE_FORMAT);
        date.add(today + "-" + startTime);
        date.add(today + "-" + endTime);
        try {
            employee.registerTime("Work", date, projectName, activityName, projectSystem);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the employee has worked {int} hours on {string} under {string}")
    public void theEmployeeHasWorkedHoursOnUnder(int expectedHours, String activityName, String projectName) {
        activity = project.getActivityByName(activityName);
        int total = (int) activity.getRegisteredTime();
        assertEquals(expectedHours, total);
    }

    @When("the employee later corrects time spent in the system to say he started to work on {string} under {string} at {string} and stopped at {string}")
    public void theEmployeeLaterCorrectsTimeSpent(String activityName, String projectName, String correctedStart, String correctedEnd) {
        ArrayList<String> date = new ArrayList<>();
        String today = LocalDate.now().format(DATE_FORMAT);
        date.add(today + "-" + correctedStart);
        date.add(today + "-" + correctedEnd);
        try {
            employee.registerTime("Work", date, projectName, activityName, projectSystem);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the employee has worked {int} hours on {string} under {string} instead of {int} hours")
    public void theEmployeeHasWorkedHoursOnUnderInsteadOfHours(int expectedCorrected, String activityName, String projectName, int wrong) {
        activity = project.getActivityByName(activityName);
        int actual = (int) activity.getRegisteredTime();
        assertEquals(expectedCorrected, actual);
    }

    @When("the employee registers free time from day {string} up to and including {string}")
    public void theEmployeeRegistersFreeTime(String from, String to) {
        ArrayList<String> date = new ArrayList<>();
        date.add(from);
        date.add(to);
        try {
            employee.registerTime("FreeTime", date, "", "", projectSystem);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the employee has {int} days of free time")
    public void theEmployeeHasDaysOfFreeTime(int expectedDays) {
        long actualDays = employee.getTimeRegistry().stream()
                .filter(e -> e.getType() == TimeEntry.EntryType.FreeTime)
                .mapToLong(e -> e.getEndDate().toEpochDay() - e.getStartDate().toEpochDay() + 1)
                .sum();
        assertEquals(expectedDays, actualDays);
    }

    @And("the employee has registered work time on {string} under {string} on {string}")
    public void theEmployeeHasRegisteredWorkTimeOn(String activityName, String projectName, String dateStr) {
        ArrayList<String> date = new ArrayList<>();
        date.add(dateStr + "-09:00");
        date.add(dateStr + "-17:00");
        try {
            employee.registerTime("Work", date, projectName, activityName, projectSystem);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @And("the employee want to update the existing registration on {string}")
    public void theEmployeeWantToUpdateTheExistingRegistrationOn(String start) {
        // No-op
    }

    @When("the employee updates the registration to sick leave and gives an end date on {string}")
    public void theEmployeeUpdatesTheRegistrationToSickLeave(String end) {
        String start = employee.getTimeRegistry().stream()
                .filter(e -> e.getType() == TimeEntry.EntryType.Work)
                .map(e -> e.getStartDateTime().toLocalDate().format(DATE_FORMAT))
                .findFirst().orElseThrow();

        ArrayList<String> date = new ArrayList<>();
        date.add(start);
        date.add(end);
        try {
            employee.registerTime("SickDay", date, "", "", projectSystem);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @Then("the time in period {string} up to and including {string} is updated as sick leave")
    public void theTimeInPeriodIsUpdatedAsSickLeave(String start, String end) {
        LocalDate s = LocalDate.parse(start, DATE_FORMAT);
        LocalDate e = LocalDate.parse(end, DATE_FORMAT);
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate d = s; !d.isAfter(e); d = d.plusDays(1)) {
            dates.add(d);
        }

        for (LocalDate d : dates) {
            boolean found = employee.getTimeRegistry().stream()
                    .filter(t -> t.getType() == TimeEntry.EntryType.SickDay)
                    .anyMatch(t -> !t.getStartDate().isAfter(d) && !t.getEndDate().isBefore(d));
            assertTrue(found, "Expected sick leave on " + d);
        }
    }

    @And("an activity named {string}, start date {string} and end date {string} is prepared for time registration in project {string}")
    public void anActivityNamedStartDateAndEndDateIsPreparedForTimeRegistrationInProject(String name, String startDate, String endDate, String projectName) {
        LocalDate start = LocalDate.parse(startDate, DATE_FORMAT);
        LocalDate end = LocalDate.parse(endDate, DATE_FORMAT);
        project = projectSystem.getProjectByName(projectName);
        if (project == null) {
            throw new IllegalArgumentException("Project " + projectName + " does not exist");
        }
        String generatedId = projectSystem.generateActivityID();
        activity = new Activity(generatedId, name, start, end);
        if (project.getActivityByName(projectName) == null) {
            project.addActivity(activity);
        }
    }
}
