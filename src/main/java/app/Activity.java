package app;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Activity {
    private final String name;
    private final List<Employee> assignedEmployees = new ArrayList<>();
    private final List<TimeEntry> workEntries = new ArrayList<>();
    private int budgetedTime = 0;

    public Activity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setBudgetedTime(int budgetedTime) {
        this.budgetedTime = budgetedTime;
    }

    public int getBudgetedTime() {
        return budgetedTime;
    }

    public void addWorkEntry(TimeEntry entry) {
        if (entry.getType() != TimeEntry.EntryType.Work) {
            throw new IllegalArgumentException("Only Work entries can be added to an activity.");
        }
        workEntries.add(entry);
    }

    public int getRegisteredTime() {
        return (int) workEntries.stream()
                .mapToDouble(TimeEntry::getWorkDurationInHours)
                .sum();
    }

    public List<TimeEntry> getWorkEntries() {
        return new ArrayList<>(workEntries); // beskyt mod ekstern mutation
    }

    public void removeWorkEntryIf(Predicate<TimeEntry> predicate) {
        workEntries.removeIf(predicate);
    }

}
