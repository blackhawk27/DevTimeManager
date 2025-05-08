package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTimeJUnitTest {
    private ProjectSystem system;
    private Project project;
    private Activity activity;
    private Employee employee;

    @BeforeEach
    void setUp() {
        system = new ProjectSystem();
        system.registerEmployee("E1");
        employee = system.getEmployeeById("E1");
        employee.logIn();

        project = system.createProject("P1", LocalDate.now(), LocalDate.now().plusDays(5), 50);
        activity = new Activity("A1", "Act1", LocalDate.now(), LocalDate.now().plusDays(5));
        project.addActivity(activity);

        system.addEmployee("E1", "P1");
    }



    private ArrayList<String> timePair(String start, String end) {
        var timeList = new ArrayList<String>();
        timeList.add("08/05/2025-" + start);
        timeList.add("08/05/2025-" + end);
        return timeList;
    }

    @Test
    void throwsIfNotLoggedIn() {
        Employee e2 = new Employee("E2");
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> e2.registerTime("Work", timePair("09:00", "17:00"), "P1", "Act1", system)
        );
        assertEquals("User not logged in", ex.getMessage());
    }

    @Test
    void throwsOnInvalidType() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("InvalidType", timePair("09:00", "17:00"), "P1", "Act1", system)
        );
        assertEquals("Invalid time type", ex.getMessage());
    }

    @Test
    void throwsOnInvalidDatetimeFormat() {
        var badTime = new ArrayList<String>();
        badTime.add("invalid");
        badTime.add("08/05/2025-17:00");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", badTime, "P1", "Act1", system)
        );
        assertEquals("Invalid datetime format (dd/MM/yyyy-HH:mm)", ex.getMessage());
    }

    @Test
    void throwsIfTimeCrossesDateBoundary() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", new ArrayList<>(java.util.List.of("08/05/2025-23:00", "09/05/2025-01:00")), "P1", "Act1", system)
        );
        assertEquals("Work hours must be registered on the same date", ex.getMessage());
    }


    @Test
    void throwsOnInvalidDuration() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", timePair("10:00", "10:15"), "P1", "Act1", system)
        );
        assertEquals("Invalid work duration", ex.getMessage());
    }

    @Test
    void throwsIfProjectNotFound() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", timePair("09:00", "17:00"), "InvalidProject", "Act1", system)
        );
        assertEquals("Project or activity not found", ex.getMessage());
    }

    @Test
    void throwsIfActivityNotFound() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", timePair("09:00", "17:00"), "P1", "InvalidActivity", system)
        );
        assertEquals("Project or activity not found", ex.getMessage());
    }

    @Test
    void throwsIfEmployeeNotAssignedToActivity() {
        Employee newEmp = new Employee("E2");
        newEmp.logIn();

        // Opret projekt og aktivitet
        Project project = system.createProject("P2", LocalDate.now(), LocalDate.now().plusDays(5), 50);
        Activity activity = new Activity("A2", "Act2", LocalDate.now(), LocalDate.now().plusDays(5));
        project.addActivity(activity);

        // Ingen tildeling af employee til aktiviteten

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> newEmp.registerTime("Work", timePair("09:00", "17:00"), "P2", "Act2", system)
        );
        assertEquals("Employee is not assigned to the activity", ex.getMessage());
    }


    @Test
    void canRegisterEightHoursWork() {
        Employee emp = new Employee("E1");
        emp.logIn();
        ArrayList<String> workTime = timePair("08:00", "16:00");

        Activity act = new Activity("A001", "Coding", LocalDate.now(), LocalDate.now().plusDays(1));
        project.addActivity(act);
        act.assignEmployee(emp);

        String result = emp.registerTime("Work", workTime, "P1", "Coding", system);
        assertEquals("Registration completed", result);
    }

    @Test
    void throwsOnInvalidDateFormatForNonWork() {
        var badDates = new ArrayList<String>();
        badDates.add("foo");
        badDates.add("02/06/2025");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("SickDay", badDates, "", "", system)
        );
        assertEquals("Invalid date format (dd/MM/yyyy)", ex.getMessage());
    }

    @Test
    void throwsIfStartAfterEndNonWork() {
        ArrayList<String> dates = new ArrayList<>();
        dates.add("05/06/2025");
        dates.add("01/06/2025");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("FreeTime", dates, "", "", system)
        );
        assertEquals("Start date cannot be after end date", ex.getMessage());
    }

    @Test
    void canRegisterFreeTimeWithoutOverlap() {
        String response1 = employee.registerTime("FreeTime",
                new ArrayList<>(java.util.List.of("02/06/2025", "04/06/2025")),
                "", "", system);
        assertEquals("Absence registered", response1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("FreeTime",
                        new ArrayList<>(java.util.List.of("03/06/2025", "05/06/2025")),
                        "", "", system)
        );
        assertEquals("Overlapping time registration", ex.getMessage());
    }

    @Test
    void canRegisterSickDay() {
        String response = employee.registerTime("SickDay",
                new ArrayList<>(java.util.List.of("01/06/2025", "02/06/2025")),
                "", "", system);
        assertEquals("Absence registered", response);
    }
}
