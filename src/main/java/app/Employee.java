package app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Employee {

    public boolean loggedIn;
    private final String id;
    private String tempProjectName;
    private LocalDate tempStartDate;
    private LocalDate tempEndDate;
    private double budgetedTime;
    private int assignedActivitiesCount = 0;
    private final DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy"
    );

    private final List<TimeEntry> timeRegistry = new ArrayList<>();

    public List<TimeEntry> getTimeRegistry() {
        return timeRegistry;
    }

    public Employee(String id) {
        this.id = id;
        this.loggedIn = false;
    }

    public void logIn() {
        this.loggedIn = true;
    }

    public void logOut() {
        this.loggedIn = false;
    }

    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public String getId() {
        return this.id;
    }

    public void inputProjectName(String name) {
        tempProjectName = name;
    }

    public void inputStartDate(LocalDate date) {
        tempStartDate = date;
    }

    public void inputEndDate(LocalDate date) {
        tempEndDate = date;
    }

    public void inputBudgetedTime(double time) {
        this.budgetedTime = time;
    }

    public Project createProject(ProjectSystem system) {
        if (!loggedIn) {
            throw new IllegalArgumentException(
                "Employee must be logged in to create a project."
            );
        }

        if (tempProjectName == null || tempProjectName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Project name missing. Project has not been created."
            );
        }

        if (tempStartDate == null) {
            throw new IllegalArgumentException(
                "Invalid date format. Project has not been created."
            );
        }

        if (tempEndDate == null) {
            throw new IllegalArgumentException(
                "Invalid date format. Project has not been created."
            );
        }

        if (tempStartDate.isAfter(tempEndDate)) {
            throw new IllegalArgumentException(
                "Start date must be before or equal to end date. Project has not been created."
            );
        }

        return system.createProject(
            tempProjectName,
            tempStartDate,
            tempEndDate,
            budgetedTime
        );
    }

    public boolean canTakeMoreActivities() {
        return assignedActivitiesCount < 10;
    }

    public void addToActivity() {
        assignedActivitiesCount++;
    }

    public String registerTime(
            String type,
            ArrayList<String> date,
            String projectName,
            String activityName,
            ProjectSystem system
    ) {
        // Preconditions
        assert loggedIn : "Precondition failed: Employee must be logged in";
        assert type != null : "Precondition failed: Type must not be null";
        assert date != null : "Precondition failed: Date list must not be null";
        assert date.size() == 2 : "Precondition failed: Date list must contain exactly two elements";
        assert system != null : "Precondition failed: System must not be null";

        TimeEntry.EntryType entryType;
        try {
            entryType = TimeEntry.EntryType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ugyldig tidstype");
        }

        if (entryType == TimeEntry.EntryType.Work) {
            LocalDateTime start = parseDateTime(date.get(0));
            LocalDateTime end = parseDateTime(date.get(1));

            assert start.toLocalDate().equals(end.toLocalDate()) :
                    "Precondition failed: Work entries must be on the same date";

            double duration = (double) java.time.Duration.between(start, end).toMinutes() / 60.0;
            assert duration > 0 && duration <= 24 && duration % 0.5 == 0 :
                    "Precondition failed: Duration must be between 0 and 24 hours in 0.5 hour increments";

            Project project = system.getProjectByName(projectName);
            assert project != null : "Precondition failed: Project must exist";

            Activity activity = project.getActivityByName(activityName);
            assert activity != null : "Precondition failed: Activity must exist";
            assert activity.isEmployeeAssigned(this) :
                    "Precondition failed: Employee must be assigned to the activity";

            timeRegistry.removeIf(
                    e -> e.getType() == TimeEntry.EntryType.Work &&
                            e.getStartDateTime().toLocalDate().equals(start.toLocalDate()) &&
                            e.getProjectName().equals(projectName) &&
                            e.getActivityName().equals(activityName)
            );

            activity.removeWorkEntryIf(
                    entry -> entry.getStartDateTime().toLocalDate().equals(start.toLocalDate()) &&
                            entry.getProjectName().equals(projectName) &&
                            entry.getActivityName().equals(activityName)
            );

            TimeEntry newEntry = new TimeEntry(entryType, start, end, projectName, activityName);
            timeRegistry.add(newEntry);
            activity.addWorkEntry(newEntry);

            // Postconditions
            boolean existsInEmployee = timeRegistry.stream().anyMatch(e ->
                    e.getType() == TimeEntry.EntryType.Work &&
                            e.getStartDateTime().toLocalDate().equals(start.toLocalDate()) &&
                            e.getProjectName().equals(projectName) &&
                            e.getActivityName().equals(activityName)
            );
            assert existsInEmployee : "Postcondition failed: Time entry should be in employee's registry";

            boolean existsInActivity = activity.getWorkEntries().stream().anyMatch(e ->
                    e.getStartDateTime().toLocalDate().equals(start.toLocalDate()) &&
                            e.getProjectName().equals(projectName) &&
                            e.getActivityName().equals(activityName)
            );
            assert existsInActivity : "Postcondition failed: Time entry should be in activity's registry";

            return "Registrering gennemført";
        } else {
            LocalDate start = parseDate(date.get(0));
            LocalDate end = parseDate(date.get(1));

            assert !start.isAfter(end) : "Precondition failed: Start date must not be after end date";

            for (TimeEntry entry : timeRegistry) {
                if (entry.getType() == entryType) {
                    if (!entry.getEndDate().isBefore(start) && !entry.getStartDate().isAfter(end)) {
                        throw new IllegalArgumentException("Overlapping time registration");
                    }
                }
            }

            TimeEntry newEntry = new TimeEntry(entryType, start, end);
            timeRegistry.add(newEntry);

            // Postcondition
            boolean exists = timeRegistry.stream().anyMatch(e ->
                    e.getType() == entryType &&
                            !e.getEndDate().isBefore(start) &&
                            !e.getStartDate().isAfter(end)
            );
            assert exists : "Postcondition failed: Absence entry should be in employee's registry";

            return "Fravær registreret";
        }
    }

    private LocalDateTime parseDateTime(String input) {
        try {
            return LocalDateTime.parse(input, dateTimeFormatter);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Invalid datetime format (dd/MM/yyyy-HH:mm)"
            );
        }
    }

    private LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input, dateFormatter);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Invalid date format (dd/MM/yyyy)"
            );
        }
    }
}
