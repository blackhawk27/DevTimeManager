package app;

import app.*;
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
        // register and log in employee
        system.registerEmployee("E1");
        employee = system.getEmployeeById("E1");
        employee.logIn();

        // create project + activity + assign
        project = system.createProject("P1", LocalDate.now(), LocalDate.now().plusDays(5), 50);
        activity = new Activity("A1", "Act1", LocalDate.now(), LocalDate.now().plusDays(5));
        project.addActivity(activity);

        // add employee to project and activity
        system.addEmployee("E1", "P1");
    }

    // helper to build the two‐element list needed by registerTime
    private ArrayList<String> timePair(String a, String b) {
        var l = new ArrayList<String>();
        String today = LocalDate.now().toString();
        l.add(today + "-" + a);
        l.add(today + "-" + b);
        return l;
    }

    @Test
    void throwsIfNotLoggedIn() {
        system.addEmployee("E1", "P1", "Act1");
        Employee e2 = new Employee("E2");
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> e2.registerTime("Work", timePair("09:00","17:00"), "P1", "Act1", system)
        );
        assertEquals("User not logged in", ex.getMessage());
    }

    @Test
    void throwsOnBadType() {
        system.addEmployee("E1", "P1", "Act1");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Foo", timePair("09:00","17:00"), "P1", "Act1", system)
        );
        assertEquals("Invalid datetime format", ex.getMessage());
    }

    @Test
    void throwsOnBadDatetimeFormat() {
        system.addEmployee("E1", "P1", "Act1");
        var bad = new ArrayList<String>();
        bad.add("bad");
        bad.add("17:00");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", bad, "P1", "Act1", system)
        );
        assertEquals("Invalid datetime format (dd/MM/yyyy-HH:mm)", ex.getMessage());
    }

    @Test
    void throwsIfCrossesDateBoundaries() {
        system.addEmployee("E1", "P1", "Act1");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", timePair("23:00","01:00"), "P1", "Act1", system)
        );
        assertEquals("Invalid datetime format (dd/MM/yyyy-HH:mm)", ex.getMessage());
    }

    @Test
    void throwsOnInvalidDuration() {
        system.addEmployee("E1", "P1", "Act1");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", timePair("10:00","10:15"), "P1", "Act1", system)
        );
        assertEquals("Invalid datetime format (dd/MM/yyyy-HH:mm)", ex.getMessage());
    }

    @Test
    void throwsIfProjectNotFound() {
        system.addEmployee("E1", "P1", "Act1");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", timePair("09:00","17:00"), "NOPE", "Act1", system)
        );
        assertEquals("Invalid datetime format (dd/MM/yyyy-HH:mm)", ex.getMessage());
    }

    @Test
    void throwsIfActivityNotFound() {
        system.addEmployee("E1", "P1", "Act1");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", timePair("09:00","17:00"), "P1", "NOPE", system)
        );
        assertEquals("Activity not found in project", ex.getMessage());
    }

    @Test
    void throwsIfEmployeeNotAssignedToActivity() {
        // unregister from activity
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("Work", timePair("09:00","17:00"), "P1", "Act1", system)
        );
        assertEquals("Employee is not assigned to the activity", ex.getMessage());
    }

    @Test
    void canRegisterEightHoursWork() {
        system.addEmployee("E1", "P1", "Act1");
        String result = employee.registerTime("Work", timePair("09:00","17:00"), "P1", "Act1", system);
        assertEquals("Fravær registreret", result);
        // the activity now accumulates 8 hours
        assertEquals(8.0, activity.getRegisteredTime());
    }

    @Test
    void throwsOnBadDateFormatNonWork() {
        system.addEmployee("E1", "P1", "Act1");
        var bad = new ArrayList<String>();
        bad.add("foo");
        bad.add("02/06/2025");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("SickDay", bad, "", "", system)
        );
        assertEquals("Invalid date format (dd/MM/yyyy)", ex.getMessage());
    }

    @Test
    void throwsOnStartAfterEndNonWork() {
        system.addEmployee("E1", "P1", "Act1");
        var dates = new ArrayList<String>();
        dates.add("05/06/2025");
        dates.add("01/06/2025");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("FreeTime", dates, "", "", system)
        );
        assertEquals("Startdate cannot be after enddate", ex.getMessage());
    }

    @Test
    void canRegisterFreeTimeOnceButNotOverlap() {
        system.addEmployee("E1", "P1", "Act1");
        // first chunk succeeds
        String r1 = employee.registerTime("FreeTime",
                new ArrayList<>(java.util.List.of("02/06/2025","04/06/2025")),
                "", "", system);
        assertEquals("Absence registered", r1);

        // overlapping chunk fails
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> employee.registerTime("FreeTime",
                        new ArrayList<>(java.util.List.of("03/06/2025","05/06/2025")),
                        "", "", system)
        );
        assertEquals("Overlapping time registration", ex.getMessage());
    }

    @Test
    void canRegisterSickDay() {
        system.addEmployee("E1", "P1", "Act1");
        String r = employee.registerTime("SickDay",
                new ArrayList<>(java.util.List.of("01/06/2025","02/06/2025")),
                "", "", system);
        assertEquals("Absence registered", r);
    }
}
