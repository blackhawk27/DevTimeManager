package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeEntryJUnitTest {

    private TimeEntry workEntry;
    private TimeEntry freeTimeEntry;
    private TimeEntry courseEntry;
    private TimeEntry sickDayEntry;

    @BeforeEach
    void setUp() {
        workEntry = new TimeEntry(
                TimeEntry.EntryType.Work,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "Project1",
                "Activity1"
        );

        freeTimeEntry = new TimeEntry(
                TimeEntry.EntryType.FreeTime,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        courseEntry = new TimeEntry(
                TimeEntry.EntryType.Course,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        sickDayEntry = new TimeEntry(
                TimeEntry.EntryType.SickDay,
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );
    }

    @Test
    void testGetType() {
        assertEquals(TimeEntry.EntryType.Work, workEntry.getType());
        assertEquals(TimeEntry.EntryType.FreeTime, freeTimeEntry.getType());
        assertEquals(TimeEntry.EntryType.Course, courseEntry.getType());
        assertEquals(TimeEntry.EntryType.SickDay, sickDayEntry.getType());
    }

    @Test
    void testGetStartDateTime() {
        assertNotNull(workEntry.getStartDateTime());
        assertNull(freeTimeEntry.getStartDateTime());
        assertNull(courseEntry.getStartDateTime());
        assertNull(sickDayEntry.getStartDateTime());
    }

    @Test
    void testGetEndDateTime() {
        assertNotNull(workEntry.getEndDateTime());
        assertNull(freeTimeEntry.getEndDateTime());
        assertNull(courseEntry.getEndDateTime());
        assertNull(sickDayEntry.getEndDateTime());
    }

    @Test
    void testGetStartDate() {
        assertNull(workEntry.getStartDate());
        assertNotNull(freeTimeEntry.getStartDate());
        assertNotNull(courseEntry.getStartDate());
        assertNotNull(sickDayEntry.getStartDate());
    }

    @Test
    void testGetEndDate() {
        assertNull(workEntry.getEndDate());
        assertNotNull(freeTimeEntry.getEndDate());
        assertNotNull(courseEntry.getEndDate());
        assertNotNull(sickDayEntry.getEndDate());
    }

    @Test
    void testGetProjectName() {
        assertEquals("Project1", workEntry.getProjectName());
        assertNull(freeTimeEntry.getProjectName());
        assertNull(courseEntry.getProjectName());
        assertNull(sickDayEntry.getProjectName());
    }

    @Test
    void testGetActivityName() {
        assertEquals("Activity1", workEntry.getActivityName());
        assertNull(freeTimeEntry.getActivityName());
        assertNull(courseEntry.getActivityName());
        assertNull(sickDayEntry.getActivityName());
    }

    @Test
    void testGetWorkDurationInHours() {
        assertEquals(2.0, workEntry.getWorkDurationInHours(), 0.01);
        assertEquals(0.0, freeTimeEntry.getWorkDurationInHours(), 0.01);
        assertEquals(0.0, courseEntry.getWorkDurationInHours(), 0.01);
        assertEquals(0.0, sickDayEntry.getWorkDurationInHours(), 0.01);
    }

    @Test
    void testInvalidWorkEntryDuration() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusHours(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new TimeEntry(TimeEntry.EntryType.Work, start, end, "Project1", "Activity1")
        );
        assertEquals("End time must be after start time for work entry.", ex.getMessage());
    }

    @Test
    void testInvalidNonWorkEntryDuration() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.minusDays(1);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new TimeEntry(TimeEntry.EntryType.FreeTime, start, end)
        );
        assertEquals("End date must be after start date for non-work entry.", ex.getMessage());
    }

}
