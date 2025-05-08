package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ActivityJUnitTest {

    private Activity activity;
    private Employee emp1;
    private Employee emp2;
    private TimeEntry workEntry1;
    private TimeEntry workEntry2;
    private TimeEntry nonWorkEntry;

    @BeforeEach
    void setUp() {
        activity = new Activity("A001", "Development", LocalDate.now(), LocalDate.now().plusDays(5));
        emp1 = new Employee("emp1");
        emp2 = new Employee("emp2");
        workEntry1 = new TimeEntry(TimeEntry.EntryType.Work, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Project1", "A001");
        workEntry2 = new TimeEntry(TimeEntry.EntryType.Work, LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Project1", "A001");
        nonWorkEntry = new TimeEntry(TimeEntry.EntryType.FreeTime, LocalDate.now(), LocalDate.now().plusDays(1));
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("A001", activity.getId());
        assertEquals("Development", activity.getName());
        assertEquals(LocalDate.now(), activity.getStartDate());
        assertEquals(LocalDate.now().plusDays(5), activity.getEndDate());
        assertEquals(0, activity.getBudgetedTime());
        assertEquals(0, activity.getRegisteredTime());
        assertTrue(activity.getWorkEntries().isEmpty());
    }

    @Test
    void testAssignEmployee() {
        assertFalse(activity.isEmployeeAssigned(emp1));
        activity.assignEmployee(emp1);
        assertTrue(activity.isEmployeeAssigned(emp1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> activity.assignEmployee(emp1));
        assertEquals("emp1 is already assigned to Development", ex.getMessage());

        // Assign emp2 to 10 activities
        for (int i = 0; i < 10; i++) {
            Activity tempActivity = new Activity("A" + i, "Dummy" + i, LocalDate.now(), LocalDate.now().plusDays(5));
            tempActivity.assignEmployee(emp2);
        }

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> activity.assignEmployee(emp2));
        assertEquals("emp2 cannot be assigned to more than 10 activities", ex2.getMessage());
    }


    @Test
    void testSetAndGetBudgetedTime() {
        activity.setBudgetedTime(15.0);
        assertEquals(15, activity.getBudgetedTime());
    }

    @Test
    void testAddWorkEntry() {
        activity.addWorkEntry(workEntry1);
        assertEquals(2, activity.getRegisteredTime());
        assertEquals(List.of(workEntry1), activity.getWorkEntries());

        activity.addWorkEntry(workEntry2);
        assertEquals(3, activity.getRegisteredTime());
        assertEquals(List.of(workEntry1, workEntry2), activity.getWorkEntries());
    }

    @Test
    void testAddInvalidWorkEntry() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> activity.addWorkEntry(nonWorkEntry));
        assertEquals("Only Work entries can be added to an activity.", ex.getMessage());
    }

    @Test
    void testGetRegisteredTime() {
        assertEquals(0, activity.getRegisteredTime());
        activity.addWorkEntry(workEntry1);
        assertEquals(2, activity.getRegisteredTime());
        activity.addWorkEntry(workEntry2);
        assertEquals(3, activity.getRegisteredTime());
    }

    @Test
    void testRemoveWorkEntryIf() {
        activity.addWorkEntry(workEntry1);
        activity.addWorkEntry(workEntry2);

        Predicate<TimeEntry> removeCondition = entry -> entry.getWorkDurationInHours() == 2;
        activity.removeWorkEntryIf(removeCondition);

        assertEquals(1, activity.getRegisteredTime());
        assertEquals(List.of(workEntry2), activity.getWorkEntries());
    }

    @Test
    void testGetWorkEntries() {
        assertTrue(activity.getWorkEntries().isEmpty());
        activity.addWorkEntry(workEntry1);
        assertEquals(1, activity.getWorkEntries().size());
        activity.addWorkEntry(workEntry2);
        assertEquals(2, activity.getWorkEntries().size());
    }


}
