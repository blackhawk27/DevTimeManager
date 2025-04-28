package app;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private final String name;
    private final List<Employee> assignedEmployees = new ArrayList<>();

    public Activity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void assignEmployee(Employee employee) {
        assignedEmployees.add(employee);
    }

    public boolean isEmployeeAssigned(Employee employee) {
        return assignedEmployees.contains(employee);
    }
}
