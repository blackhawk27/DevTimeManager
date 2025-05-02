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
    private int assignedActivitiesCount = 0;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    private final List<TimeEntry> timeRegistry = new ArrayList<>();

    public List<TimeEntry> getTimeRegistry() {
        return timeRegistry;
    }


    public Employee(String id) {
        this.id = id;
        this.loggedIn = false;
    }


    public void logIn(){
        this.loggedIn = true;
    }

    public void logOut(){
        this.loggedIn = false;
    }

    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    public String getId(){
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

    public Project createProject(ProjectSystem system) {
        if (!loggedIn) {
            throw new IllegalArgumentException("Employee must be logged in to create a project.");
        }

        if (tempProjectName == null || tempProjectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name missing. Project has not been created.");
        }

        if (tempStartDate == null) {
            throw new IllegalArgumentException("Invalid date format. Project has not been created.");
        }

        if (tempEndDate == null) {
            throw new IllegalArgumentException("Invalid date format. Project has not been created.");
        }

        if (tempStartDate.isAfter(tempEndDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date. Project has not been created.");
        }

        return system.createProject(tempProjectName, tempStartDate, tempEndDate);
    }

    public boolean canTakeMoreActivities() {
        return assignedActivitiesCount < 10;
    }

    public void addToActivity() {
        assignedActivitiesCount++;
    }

    public String registerTime(String type, ArrayList<String> date, String projectName, String activityName, ProjectSystem system) {
        if (!loggedIn) {
            throw new IllegalStateException("Bruger ikke logget ind");
        }

        TimeEntry.EntryType entryType;
        try {
            entryType = TimeEntry.EntryType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ugyldig tidstype");
        }

        if (date == null || date.size() != 2) {
            throw new IllegalArgumentException("Dato skal indeholde præcis to elementer");
        }

        if (entryType == TimeEntry.EntryType.Work) {
            // Format: "YYYY-MM-DD-HH:mm"
            LocalDateTime start = parseDateTime(date.get(0));
            LocalDateTime end = parseDateTime(date.get(1));

            if (!start.toLocalDate().equals(end.toLocalDate())) {
                throw new IllegalArgumentException("Arbejdstid skal være på samme dato");
            }

            double duration = (double) java.time.Duration.between(start, end).toMinutes() / 60.0;
            if (duration <= 0 || duration > 24 || duration % 0.5 != 0) {
                throw new IllegalArgumentException("Ugyldig arbejdstid");
            }

            Project project = system.getProjectByName(projectName);
            if (project == null) throw new IllegalArgumentException("Projekt eller aktivitet ikke fundet");

            Activity activity = project.getActivityByName(activityName);
            if (activity == null || !activity.isEmployeeAssigned(this)) {
                throw new IllegalArgumentException("Projekt eller aktivitet ikke fundet");
            }

            // Før du tilføjer ny entry, fjern gammel både fra employee og activity
            timeRegistry.removeIf(e ->
                    e.getType() == TimeEntry.EntryType.Work &&
                            e.getStartDateTime().toLocalDate().equals(start.toLocalDate()) &&
                            e.getProjectName().equals(projectName) &&
                            e.getActivityName().equals(activityName)
            );

            activity.removeWorkEntryIf(entry ->
                    entry.getStartDateTime().toLocalDate().equals(start.toLocalDate()) &&
                            entry.getProjectName().equals(projectName) &&
                            entry.getActivityName().equals(activityName)
            );

            TimeEntry newEntry = new TimeEntry(entryType, start, end, projectName, activityName);
            timeRegistry.add(newEntry);
            activity.addWorkEntry(newEntry);
            return "Registrering gennemført";
        } else {
            // Format: "YYYY-MM-DD"
            LocalDate start = parseDate(date.get(0));
            LocalDate end = parseDate(date.get(1));

            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Startdato må ikke være efter slutdato");
            }

            // Tjek for overlap
            for (TimeEntry entry : timeRegistry) {
                if (entry.getType() == entryType) {
                    if (!entry.getEndDate().isBefore(start) && !entry.getStartDate().isAfter(end)) {
                        throw new IllegalArgumentException("Overlapping time registration");
                    }
                }
            }

            TimeEntry newEntry = new TimeEntry(entryType, start, end);
            timeRegistry.add(newEntry);
            return "Fravær registreret";
        }
    }


    private LocalDateTime parseDateTime(String input) {
        try {
            return LocalDateTime.parse(input, dateTimeFormatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid datetime format (dd/MM/yyyy-HH:mm)");
        }
    }

    private LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input, dateFormatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format (dd/MM/yyyy)");
        }
    }

}