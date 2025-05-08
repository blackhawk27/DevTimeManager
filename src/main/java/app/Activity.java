package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Activity {
    private final String id;
    private final String name;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<Employee> assignedEmployees = new ArrayList<>();
    private final List<TimeEntry> workEntries = new ArrayList<>();
    private Double budgetedTime = 0.0;

    public Activity(String id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void assignEmployee(Employee employee) {
        if (isEmployeeAssigned(employee)) {
            throw new IllegalArgumentException(employee.getId() + " is already assigned to " + name);
        }
        if (!employee.canTakeMoreActivities()) {
            throw new IllegalArgumentException(employee.getId() + " cannot be assigned to more than 10 activities");
        }
        assignedEmployees.add(employee);
        employee.addToActivity();
    }

    public boolean isEmployeeAssigned(Employee employee) {
        return assignedEmployees.contains(employee);
    }

    public void setBudgetedTime(Double budgetedTime) {
        this.budgetedTime = budgetedTime;
    }

    public Double getBudgetedTime() {
        return budgetedTime;
    }

    public void addWorkEntry(TimeEntry entry) {
        if (entry.getType() != TimeEntry.EntryType.Work) {
            throw new IllegalArgumentException("Only Work entries can be added to an activity.");
        }
        workEntries.add(entry);
    }

    public double getRegisteredTime() {
        return workEntries.stream()
                .mapToDouble(TimeEntry::getWorkDurationInHours)
                .sum();
    }

    public List<TimeEntry> getWorkEntries() {
        return new ArrayList<>(workEntries);
    }

    public void removeWorkEntryIf(Predicate<TimeEntry> predicate) {
        workEntries.removeIf(predicate);
    }
}
