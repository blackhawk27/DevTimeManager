package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProjectSystem projectSystem = new ProjectSystem();
    private static Employee currentEmployee = null;

    public static void main(String[] args) {
        projectSystem.registerEmployee("teem");
        projectSystem.registerEmployee("huba");
        projectSystem.registerEmployee("pby");

        // Pre-create a project named "p1"
        Project defaultProject = projectSystem.createProject(
            "p1",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 12, 31),
            100.0
        );

        // Assign "teem" to the project and make them the project manager
        projectSystem.addEmployee("teem", "p1");
        defaultProject.assignProjectManager(
            projectSystem.getEmployeeById("teem"),
            new ProjectManager("teem")
        );

        // Pre-create an activity named "a1" in the project
        Activity defaultActivity = new Activity(
            projectSystem.generateActivityID(),
            "a1",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 12, 31)
        );
        defaultProject.addActivity(defaultActivity);

        // Assign "teem" to the activity
        projectSystem.addEmployee("teem", "p1", "a1");

        System.out.println(
            "Welcome to the Project System Management for Softwarehuset A/S"
        );
        while (true) {
            if (currentEmployee == null || !currentEmployee.loggedIn) {
                System.out.println(
                    "Type the number from the following options:"
                );
                System.out.println("1 - Log In");
            } else {
                System.out.println(
                    "Type the number from the following options:"
                );
                System.out.println("2 - Create Project");
                System.out.println("3 - Add Employee to Project");
                System.out.println("4 - Create Activity");
                System.out.println("5 - Assign Employee to Activity");
                System.out.println("6 - Register Time");
                System.out.println("7 - Generate Project Report");
                System.out.println("8 - Assign Project Manager");
                System.out.println("9 - Exit");
            }
            String option = scanner.nextLine();
            try {
                if (currentEmployee == null || !currentEmployee.loggedIn) {
                    switch (option) {
                        case "1" -> logIn();
                        default -> System.out.println("Invalid option");
                    }
                } else {
                    switch (option) {
                        case "2" -> createProject();
                        case "3" -> assignEmployeeToProject();
                        case "4" -> createActivity();
                        case "5" -> assignEmployeeToActivity();
                        case "6" -> registerTime();
                        case "7" -> generateProjectReport();
                        case "8" -> assignProjectManager();
                        case "9" -> {
                            System.out.println(
                                "Thank you for using Project System Management"
                            );
                            return;
                        }
                        default -> System.out.println("Invalid option");
                    }
                }
            } catch (RuntimeException e) {
                System.out.println("Returning to main menu...");
            }
        }
    }

    private static void logIn() {
        String id = prompt(
            "Please enter your four letter employee ID"
        ).toLowerCase();
        if (!projectSystem.isRegistered(id)) {
            System.out.println("Invalid employee ID");
            return;
        }
        currentEmployee = projectSystem.getEmployeeById(id);
        currentEmployee.logIn();
        System.out.println(
            "You're now logged in as employee: " + currentEmployee.getId()
        );
    }

    private static void createProject() {
        try {
            String projectName = prompt(
                "Please enter your project name (ex. Greg's Website)"
            );
            currentEmployee.inputProjectName(projectName);
            LocalDate startDate = LocalDate.parse(
                prompt("Please enter start date (dd/MM/yyyy)"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );
            currentEmployee.inputStartDate(startDate);
            LocalDate endDate = LocalDate.parse(
                prompt("Please enter end date (dd/MM/yyyy)"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );
            currentEmployee.inputEndDate(endDate);
            double budgetedTime = Double.parseDouble(
                prompt("Please enter budgeted time in hours (e.g. 120.5)")
            );
            currentEmployee.inputBudgetedTime(budgetedTime);
            var project = currentEmployee.createProject(projectSystem);
            System.out.println(
                "Project " +
                project.getName() +
                " created successfully with id " +
                project.getId()
            );
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void assignEmployeeToProject() {
        String projectName = prompt("Enter project name");
        String employeeId = prompt("Enter employee ID to add");
        try {
            projectSystem.addEmployee(employeeId, projectName);
            System.out.println(
                "Employee " + employeeId + " added to project " + projectName
            );
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void assignEmployeeToActivity() {
        String projectName = prompt("Enter project name");
        String activityName = prompt("Enter activity name");
        String employeeId = prompt("Enter employee ID to assign");
        try {
            projectSystem.addEmployee(employeeId, projectName, activityName);
            System.out.println(
                "Employee " +
                employeeId +
                " assigned to activity " +
                activityName +
                " in project " +
                projectName
            );
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void assignProjectManager() {
        String projectName = prompt("Enter project name");
        String managerId = prompt(
            "Enter ID of the employee to assign as project manager"
        );

        Project project = projectSystem.getProjectByName(projectName);
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        if (!projectSystem.isRegistered(managerId)) {
            System.out.println(
                "Error: " + managerId + " is not registered as an employee."
            );
            return;
        }

        ProjectManager newManager = new ProjectManager(managerId);

        try {
            project.assignProjectManager(currentEmployee, newManager);
            System.out.println(
                managerId + " is now project manager for " + project.getName()
            );
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registerTime() {
        String choice = prompt(
            "Do you want to:\n1 - Register new time\n2 - Update existing time entry"
        );
        switch (choice) {
            case "1" -> registerNewTime();
            case "2" -> updateTimeEntry();
            default -> System.out.println("Invalid choice");
        }
    }

    private static void registerNewTime() {
        String type = prompt(
            "Type of entry (Work / SickDay / FreeTime / Course)"
        );
        ArrayList<String> dateInputs = new ArrayList<>();

        if (type.equalsIgnoreCase("Work")) {
            String projectName = prompt("Project name");
            String activityName = prompt("Activity name");
            dateInputs.add(prompt("Start time (dd/MM/yyyy-HH:mm)"));
            dateInputs.add(prompt("End time (dd/MM/yyyy-HH:mm)"));
            try {
                currentEmployee.registerTime(
                    "Work",
                    dateInputs,
                    projectName,
                    activityName,
                    projectSystem
                );
                System.out.println("Work time registered.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            dateInputs.add(prompt("Start date (dd/MM/yyyy)"));
            dateInputs.add(prompt("End date (dd/MM/yyyy)"));
            try {
                currentEmployee.registerTime(
                    type,
                    dateInputs,
                    "",
                    "",
                    projectSystem
                );
                System.out.println(type + " registered.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void updateTimeEntry() {
        String type = prompt(
            "Which type do you want to update?\nOptions: Work / SickDay / FreeTime / Course"
        );
        ArrayList<String> dateInputs = new ArrayList<>();

        if (type.equalsIgnoreCase("Work")) {
            String projectName = prompt("Project name");
            String activityName = prompt("Activity name");
            dateInputs.add(prompt("New start time (dd/MM/yyyy-HH:mm)"));
            dateInputs.add(prompt("New end time (dd/MM/yyyy-HH:mm)"));
            try {
                currentEmployee.registerTime(
                    "Work",
                    dateInputs,
                    projectName,
                    activityName,
                    projectSystem
                );
                System.out.println("Work time card updated.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            dateInputs.add(prompt("New start date (dd/MM/yyyy)"));
            dateInputs.add(prompt("New end date (dd/MM/yyyy)"));
            try {
                currentEmployee.registerTime(
                    type,
                    dateInputs,
                    "",
                    "",
                    projectSystem
                );
                System.out.println(type + " updated.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createActivity() {
        try {
            String projectName = prompt("Enter the name of the project");
            Project project = projectSystem.getProjectByName(projectName);
            if (project == null) {
                System.out.println("Project not found.");
                return;
            }

            String name = prompt("Enter activity name");
            LocalDate start = LocalDate.parse(
                prompt("Enter start date (dd/MM/yyyy)"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );
            LocalDate end = LocalDate.parse(
                prompt("Enter end date (dd/MM/yyyy)"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );

            if (start.isAfter(end)) {
                System.out.println(
                    "Error: End date cannot be before start date"
                );
                return;
            }

            if (project.getActivityByName(name) != null) {
                System.out.println(
                    "Error: Activity with name '" +
                    name +
                    "' already exists in project '" +
                    projectName +
                    "'"
                );
                return;
            }

            String generatedId = projectSystem.generateActivityID();
            Activity activity = new Activity(generatedId, name, start, end);
            project.addActivity(activity);
            System.out.println(
                "Activity '" +
                name +
                "' created with ID: " +
                generatedId +
                " in project '" +
                projectName +
                "'"
            );
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generateProjectReport() {
        System.out.print("Enter project name to generate report: ");
        String projectName = scanner.nextLine();

        Project project = projectSystem.getProjectByName(projectName);
        if (project == null) {
            System.out.println("Error: Project not found.");
            return;
        }

        List<Activity> activities = project.getActivities();

        int totalBudgetedTime = (int) project.getBudgetedTime();
        int totalRegisteredTime = activities
            .stream()
            .mapToInt(Activity::getRegisteredTime)
            .sum();
        int unallocatedHours = totalBudgetedTime - totalRegisteredTime;
        int estimatedRemaining = Math.max(unallocatedHours, 0);

        System.out.println(
            "\n--- Project Report for '" + projectName + "' ---"
        );
        System.out.println("");
        System.out.println("Project ID: " + project.getId());
        System.out.println("Project Manager: " + project.getProjectManager());
        System.out.println("Start Date: " + project.getStartDate());
        System.out.println("End Date: " + project.getEndDate());
        System.out.println();
        System.out.println("Total Registered Hours: " + totalRegisteredTime);
        System.out.println("Budgeted Time: " + totalBudgetedTime);
        System.out.println("Unallocated Hours: " + unallocatedHours);
        System.out.println(
            "Estimated Remaining Work Time: " + estimatedRemaining
        );

        if (totalRegisteredTime == 0) {
            System.out.println(
                "Warning: No work has been registered on this project yet."
            );
        }

        if (totalRegisteredTime > totalBudgetedTime) {
            System.out.println("Project is over budget!");
        } else {
            System.out.println("Project is within budget.");
            System.out.println("");
        }

        System.out.println("Assigned Employees:");
        for (Employee employee : project.getEmployees()) {
            System.out.println(
                "- " +
                employee.getId() +
                " (" +
                employee.getTimeRegistry().size() +
                " time entries)"
            );
        }
        System.out.println();

        System.out.println("Activities:");
        if (project.getActivities().isEmpty()) {
            System.out.println("No activities assigned yet.");
        } else {
            for (Activity activity : project.getActivities()) {
                System.out.println("- " + activity.getName());
            }
        }
        System.out.println();

        System.out.println("Time Entries:");
        for (Employee employee : project.getEmployees()) {
            System.out.println("  Employee: " + employee.getId());
            for (TimeEntry timeEntry : employee.getTimeRegistry()) {
                System.out.println("    Type: " + timeEntry.getType());
                if (timeEntry.getType() == TimeEntry.EntryType.Work) {
                    System.out.println(
                        "    Project: " + timeEntry.getProjectName()
                    );
                    System.out.println(
                        "    Activity: " + timeEntry.getActivityName()
                    );
                    System.out.println(
                        "    Start: " +
                        timeEntry.getStartDateTime() +
                        " End: " +
                        timeEntry.getEndDateTime()
                    );
                    System.out.println(
                        "    Duration: " +
                        timeEntry.getWorkDurationInHours() +
                        " hours"
                    );
                } else {
                    System.out.println(
                        "    Start: " +
                        timeEntry.getStartDate() +
                        " End: " +
                        timeEntry.getEndDate()
                    );
                }
            }
        }
        System.out.println();
    }

    private static String prompt(String message) {
        System.out.print(message + ": ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("q")) {
            throw new RuntimeException("Cancelled");
        }
        return input;
    }
}
