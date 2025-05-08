package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Project {

    private String name;
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budgetedTime;
    private List<Employee> employees = new ArrayList<>();
    private final List<Activity> activities = new ArrayList<>();
    private ProjectManager projectManager;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy"
    );

    public Project(
        String name,
        String id,
        LocalDate start,
        LocalDate end,
        Double budgetedTime
    ) {
        this.name = name;
        this.id = id;
        this.startDate = start;
        this.endDate = end;
        this.budgetedTime = budgetedTime;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void addActivity(Activity activity) {
        if (getActivityByName(activity.getName()) != null) {
            throw new IllegalArgumentException(
                "Activity with name '" +
                activity.getName() +
                "' already exists in project '" +
                name +
                "'"
            );
        }
        activities.add(activity);
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();

        int totalBudgetedTime = (int) this.getBudgetedTime();
        int totalRegisteredTime =
            this.getActivities()
                .stream()
                .mapToInt(Activity::getRegisteredTime)
                .sum();
        int unallocatedHours = totalBudgetedTime - totalRegisteredTime;
        int estimatedRemaining = Math.max(unallocatedHours, 0);

        report
            .append("\n--- Project Report for '")
            .append(this.getName())
            .append("' ---\n\n");
        report.append("Project ID: ").append(this.getId()).append("\n");
        report
            .append("Project Manager: ")
            .append(this.getProjectManager())
            .append("\n");
        report.append("Start Date: ").append(this.getStartDate()).append("\n");
        report.append("End Date: ").append(this.getEndDate()).append("\n\n");
        report
            .append("Total Registered Hours: ")
            .append(totalRegisteredTime)
            .append("\n");
        report.append("Budgeted Time: ").append(totalBudgetedTime).append("\n");
        report
            .append("Unallocated Hours: ")
            .append(unallocatedHours)
            .append("\n");
        report
            .append("Estimated Remaining Work Time: ")
            .append(estimatedRemaining)
            .append("\n\n");

        if (totalRegisteredTime == 0) {
            report.append(
                "Warning: No work has been registered on this project yet.\n"
            );
        }

        if (totalRegisteredTime > totalBudgetedTime) {
            report.append("Project is over budget!\n");
        } else {
            report.append("Project is within budget.\n\n");
        }

        report.append("Assigned Employees:\n");
        for (Employee employee : this.getEmployees()) {
            report
                .append("- ")
                .append(employee.getId())
                .append(" (")
                .append(employee.getTimeRegistry().size())
                .append(" time entries)\n");
        }
        report.append("\n");

        report.append("Activities:\n");
        if (this.getActivities().isEmpty()) {
            report.append("No activities assigned yet.\n");
        } else {
            for (Activity activity : this.getActivities()) {
                report.append("- ").append(activity.getName()).append("\n");
            }
        }
        report.append("\n");

        report.append("Time Entries:\n");
        for (Employee employee : this.getEmployees()) {
            report.append("  Employee: ").append(employee.getId()).append("\n");
            for (TimeEntry timeEntry : employee.getTimeRegistry()) {
                report
                    .append("    Type: ")
                    .append(timeEntry.getType())
                    .append("\n");
                if (timeEntry.getType() == TimeEntry.EntryType.Work) {
                    report
                        .append("    Project: ")
                        .append(timeEntry.getProjectName())
                        .append("\n");
                    report
                        .append("    Activity: ")
                        .append(timeEntry.getActivityName())
                        .append("\n");
                    report
                        .append("    Start: ")
                        .append(timeEntry.getStartDateTime())
                        .append(" End: ")
                        .append(timeEntry.getEndDateTime())
                        .append("\n");
                    report
                        .append("    Duration: ")
                        .append(timeEntry.getWorkDurationInHours())
                        .append(" hours\n");
                } else {
                    report
                        .append("    Start: ")
                        .append(timeEntry.getStartDate())
                        .append(" End: ")
                        .append(timeEntry.getEndDate())
                        .append("\n");
                }
            }
        }
        report.append("\n");

        return report.toString();
    }

    public Double getBudgetedTime() {
        return budgetedTime;
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public Activity getActivityByName(String activityName) {
        for (Activity activity : activities) {
            if (activity.getName().equals(activityName)) {
                return activity;
            }
        }
        return null;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setProjectManager(ProjectManager manager) {
        this.projectManager = manager;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void assignProjectManager(
        Employee assigningEmployee,
        ProjectManager newManager
    ) {
        if (this.projectManager != null) {
            throw new IllegalStateException(
                "This project already has a project manager. You cannot assign a new one."
            );
        }
        if (!employees.contains(assigningEmployee)) {
            throw new IllegalStateException(
                assigningEmployee.getId() +
                " is not assigned to the project and cannot assign a manager."
            );
        }

        if (
            !employees
                .stream()
                .anyMatch(e -> e.getId().equals(newManager.getId()))
        ) {
            throw new IllegalStateException(
                newManager.getId() + " is not registered as an employee."
            );
        }

        this.projectManager = newManager;
        newManager.addProject(this);
    }
}
