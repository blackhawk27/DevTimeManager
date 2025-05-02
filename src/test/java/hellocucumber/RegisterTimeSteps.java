package hellocucumber;

import app.*;
import io.cucumber.java.en.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Given("an employee with id {string} is logged in")
    public void anEmployeeWithIdIsLoggedIn(String id) {
        projectSystem = new ProjectSystem();
        employee = new Employee(id);
        employee.logIn();

        // Opret projekt og aktivitet én gang til alle scenarier
        project = projectSystem.createProject("ProjectY", java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(30));
        activity = new Activity("ActivityX");
        project.addActivity(activity);
        project.addEmployee(employee);
        activity.assignEmployee(employee);
    }

    @And("that the employee is assigned to an {string} under {string}")
    public void thatTheEmployeeIsAssignedToAnUnder(String activityName, String projectName) {
        // Foruddefineret i Given ovenfor – ingen handling nødvendig her
    }


    @When("the employee registers in the system that he started to work on {string} under {string} at {string} and stopped at {string}")
    public void theEmployeeRegistersInTheSystemThatHeStartedToWork(String activityName, String projectName, String startTime, String endTime) {
        ArrayList<String> date = new ArrayList<>();
        String today = java.time.LocalDate.now().toString();
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
        int total = (int) activity.getRegisteredTime();
        assertEquals(expectedHours, total);
    }

    @When("the employee later corrects time spent in the system to say he started to work on {string} under {string} at {string} and stopped at {string}")
    public void theEmployeeLaterCorrectsTimeSpent(String activityName, String projectName, String correctedStart, String correctedEnd) {
        ArrayList<String> date = new ArrayList<>();
        String today = java.time.LocalDate.now().toString();
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
        // Gemmes i næste step – no-op
    }

    @When("the employee updates the registration to sick leave and gives an end date on {string}")
    public void theEmployeeUpdatesTheRegistrationToSickLeave(String end) {
        String start = employee.getTimeRegistry().stream()
                .filter(e -> e.getType() == TimeEntry.EntryType.Work)
                .map(e -> e.getStartDateTime().toLocalDate().toString())
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
        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);
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
}
