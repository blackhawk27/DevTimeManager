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
        System.out.println("Welcome to the Project System Management for Softwarehuset A/S");
        while(true) {
            if (currentEmployee == null || !currentEmployee.loggedIn) {
                System.out.println("Type the number from the following options:");
                System.out.println("1 - Log In");
            } else {
                System.out.println("Type the number from the following options:");
                System.out.println("2 - Create Project");
                System.out.println("3 - Close Program");
            }
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> logIn();
                case "2" -> createProject();
                case "3" -> {
                    System.out.println("Thank you for using Project System Mangement");
                    return;}
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void logIn() {
        System.out.println("Please enter your employee id:");
        String id = scanner.nextLine();
        if (!projectSystem.isRegistered(id)) {
            System.out.println("Invalid employee id");
            return;
        } else {
            currentEmployee = projectSystem.getEmployeeById(id);
            currentEmployee.logIn();
            System.out.println("You're now logged in as employee: teem");
        }
    }
    private static void createProject() {
        try {
            System.out.println("Please enter your project name:");
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
}
