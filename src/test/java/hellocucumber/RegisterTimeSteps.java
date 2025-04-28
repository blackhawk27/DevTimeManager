package hellocucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTimeSteps {

    private String loggedInEmployeeId;
    private Map<String, WorkSession> workSessions = new HashMap<>();
    private Map<LocalDate, String> dayRegistrations = new HashMap<>();
    private int calculatedWorkHours;
    private int registeredFreeDays;
    private LocalDate sickLeaveStart;
    private LocalDate sickLeaveEnd;

    @And("that the employee is assigned to an {string} under {string}")
    public void thatTheEmployeeIsAssignedToAnUnder(String activityName, String projectName) {
        // No-op for assignment simulation
    }

    @When("the employee registers in the system that he started to work on {string} under {string} at {string} and stopped at {string}")
    public void theEmployeeRegistersInTheSystemThatHeStartedToWorkOnUnderAtAndStoppedAt(String activityName, String projectName, String startTimeStr, String endTimeStr) {
        LocalTime start = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime end = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
        workSessions.put(activityName, new WorkSession(start, end));
        calculatedWorkHours = end.getHour() - start.getHour(); // Simplified; no cross-midnight logic
    }

    @Then("the employee has worked {int} hours on {string} under {string}")
    public void theEmployeeHasWorkedHoursOnUnder(int expectedHours, String activityName, String projectName) {
        assertEquals(expectedHours, calculatedWorkHours, "Worked hours mismatch");
    }

    @When("the employee later corrects time spent in the system to say he started to work on {string} under {string} at {string} and stopped at {string}")
    public void theEmployeeLaterCorrectsTimeSpentInTheSystemToSayHeStartedToWorkOnUnderAtAndStoppedAt(String activityName, String projectName, String correctedStartStr, String correctedEndStr) {
        LocalTime correctedStart = LocalTime.parse(correctedStartStr, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime correctedEnd = LocalTime.parse(correctedEndStr, DateTimeFormatter.ofPattern("HH:mm"));
        workSessions.put(activityName, new WorkSession(correctedStart, correctedEnd));
        calculatedWorkHours = correctedEnd.getHour() - correctedStart.getHour();
    }

    @Then("the employee has worked {int} hours on {string} under {string} instead of {int} hours")
    public void theEmployeeHasWorkedHoursOnUnderInsteadOfHours(int correctedHours, String activityName, String projectName, int wrongHours) {
        assertEquals(correctedHours, calculatedWorkHours, "Corrected worked hours mismatch");
    }

    @When("the employee registers free time from day {string} up to and including {string}")
    public void theEmployeeRegistersFreeTimeFromDayUpToAndIncluding(String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        registeredFreeDays = (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
    }

    @Then("the employee has {int} days of free time")
    public void theEmployeeHasDaysOfFreeTime(int expectedDays) {
        assertEquals(expectedDays, registeredFreeDays, "Free time days mismatch");
    }

    @And("the employee has registered work time on {string} under {string} on {string}")
    public void theEmployeeHasRegisteredWorkTimeOnUnderOn(String activityName, String projectName, String dateStr) {
        LocalDate workDate = LocalDate.parse(dateStr);
        dayRegistrations.put(workDate, "work");
    }

    @And("the employee want to update the existing registration on {string}")
    public void theEmployeeWantToUpdateTheExistingRegistrationOn(String startDateStr) {
        sickLeaveStart = LocalDate.parse(startDateStr);
    }

    @When("the employee updates the registration to sick leave and gives an end date on {string}")
    public void theEmployeeUpdatesTheRegistrationToSickLeaveAndGivesAnEndDateOn(String endDateStr) {
        sickLeaveEnd = LocalDate.parse(endDateStr);
        LocalDate current = sickLeaveStart;
        while (!current.isAfter(sickLeaveEnd)) {
            dayRegistrations.put(current, "sick leave");
            current = current.plusDays(1);
        }
    }

    @Then("the time in period {string} up to and including {string} is updated as sick leave")
    public void theTimeInPeriodUpToAndIncludingIsUpdatedAsSickLeave(String startDateStr, String endDateStr) {
        LocalDate start = LocalDate.parse(startDateStr);
        LocalDate end = LocalDate.parse(endDateStr);
        LocalDate current = start;
        while (!current.isAfter(end)) {
            assertEquals("sick leave", dayRegistrations.get(current), "Expected sick leave on " + current);
            current = current.plusDays(1);
        }
    }



    // Helper class
    private static class WorkSession {
        LocalTime start;
        LocalTime end;

        WorkSession(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }
    }
}