package app;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeEntry {
    public enum EntryType {
        Work, FreeTime, Course, SickDay
    }

    private final EntryType type;
    private final LocalDateTime startDateTime; // Bruges kun til Work
    private final LocalDateTime endDateTime;
    private final LocalDate startDate;         // Bruges kun til ikke-Work
    private final LocalDate endDate;
    private final String projectName;          // Kun relevant for Work
    private final String activityName;         // Kun relevant for Work

    // Constructor for Work
    public TimeEntry(EntryType type, LocalDateTime start, LocalDateTime end, String projectName, String activityName) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time for work entry.");
        }
        this.type = type;
        this.startDateTime = start;
        this.endDateTime = end;
        this.startDate = null;
        this.endDate = null;
        this.projectName = projectName;
        this.activityName = activityName;
    }

    // Constructor for non-Work (FreeTime, Course, SickDay)
    public TimeEntry(EntryType type, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date for non-work entry.");
        }
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startDateTime = null;
        this.endDateTime = null;
        this.projectName = null;
        this.activityName = null;
    }

    public EntryType getType() {
        return type;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getActivityName() {
        return activityName;
    }

    public double getWorkDurationInHours() {
        if (type != EntryType.Work) return 0;
        return (double) java.time.Duration.between(startDateTime, endDateTime).toMinutes() / 60.0;
    }
}
