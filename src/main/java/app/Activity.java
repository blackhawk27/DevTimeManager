package app;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private final String name;
    private final List<Employee> assignedEmployees = new ArrayList<>();

    private int budgetedTime = 0;
    private int registeredTime = 0;

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
        if(!employee.canTakeMoreActivities()){
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

    public void setRegisteredTime(int registeredTime) {
        this.registeredTime = registeredTime;
    }

    public int getRegisteredTime() {
        return registeredTime;
    }

    public int getBudgetedTime() {
        return budgetedTime;
    }
}
