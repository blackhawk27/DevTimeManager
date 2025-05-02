package app;

import app.Employee;
import app.Project;
import app.ProjectSystem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProjectSystem projectSystem = new ProjectSystem();
    private static Employee currentEmployee = null;
    private static final String firmName = "Softwarehuset A/S";

    public static void main(String[] args) {
        projectSystem.registerEmployee("teem");
        projectSystem.registerEmployee("paby");
        System.out.println("Welcome to the Project System Management for Softwarehuset A/S");
        while(true) {
            if (currentEmployee == null || !currentEmployee.loggedIn) {
                System.out.println("Type the number from the following options:");
                System.out.println("1 - Log In");
            } else {
                System.out.println("Type the number from the following options:");
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

            switch (option) {
                case "1" -> logIn();
                case "2" -> createProject();
                case "3" -> assignEmployeeToProject();
                case "4" -> createActivity();
                case "5" -> assignEmployeeToActivity();
                case "6" -> registerTime();
                case "7" -> generateProjectReport();
                case "8" -> assignProjectManager();
                case "9" -> {
                    System.out.println("Thank you for using Project System Mangement");
                    return;}
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void logIn() {
        System.out.println("Please enter your four letter employee ID:");
        String id = scanner.nextLine();
        if (!projectSystem.isRegistered(id)) {
            System.out.println("Invalid employee ID");
            return;
        } else {
            currentEmployee = projectSystem.getEmployeeById(id);
            currentEmployee.logIn();
            System.out.println("You're now logged in as employee: teem");
        }
    }
    private static void createProject() {
        try {
            System.out.println("Please enter your project name (ex. Greg's Website):");
            String projectName = scanner.nextLine();
            currentEmployee.inputProjectName(projectName);
            System.out.println("Please enter start date (dd/mm/yyyy):");
            LocalDate startDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            currentEmployee.inputStartDate(startDate);
            System.out.println("Please enter end date (dd/mm/yyyy):");
            LocalDate endDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            currentEmployee.inputEndDate(endDate);
            Project project = currentEmployee.createProject(projectSystem);
            System.out.println("Project " + project.getName() + " created successfully with id " + project.getId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addEmployeeToProject() {
        System.out.println("Please enter project ID");
        return;
    }

    private static void createActivity() {
        return;
    }

    private static void assignEmployeeToProject() {
        System.out.println("Enter project ID:");
        String projectId = scanner.nextLine();
        System.out.println("Enter employee ID to add:");
        String employeeId = scanner.nextLine();

        try {
            projectSystem.addEmployee(employeeId, projectId);
            System.out.println("Employee " + employeeId + " added to project " + projectId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void assignEmployeeToActivity() {
        System.out.println("Enter project ID:");
        String projectId = scanner.nextLine();
        System.out.println("Enter activity name:");
        String activityName = scanner.nextLine();
        System.out.println("Enter employee ID to assign:");
        String employeeId = scanner.nextLine();

        try {
            projectSystem.addEmployee(employeeId, projectId, activityName);
            System.out.println("Employee " + employeeId + " assigned to activity " + activityName + " in project " + projectId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generateProjectReport() {
        return;
    }

    private static void registerTime() {
        return;
    }

    private static void assignProjectManager() {
        System.out.println("Assign Project Manager");
        System.out.print("Enter project ID (ex. 25001): ");
        String projectId = scanner.nextLine().trim();

        Project project = projectSystem.getProjectById(projectId);
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        System.out.print("Enter ID of the employee to assign as project manager: ");
        String managerId = scanner.nextLine().trim();

        if (!projectSystem.isRegistered(managerId)) {
            System.out.println("Error: " + managerId + " is not registered as an employee.");
            return;
        }

        ProjectManager newManager = new ProjectManager(managerId);

        try {
            project.assignProjectManager(currentEmployee, newManager);
            System.out.println(managerId + " is now project manager for " + project.getName());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
