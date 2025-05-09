package hellocucumber;

import app.*;
import io.cucumber.java.en.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static hellocucumber.SharedContext.projectSystem;
import static org.junit.jupiter.api.Assertions.*;

public class CreateActivitySteps {

    // private final ProjectSystem projectSystem = AddEmployeeSteps.projectSystem;
    private Project currentProject;
    private Activity currentActivity;
    private Employee currentEmployee;
    private String errorMessage;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate parse(String date) {
        return LocalDate.parse(date, formatter);
    }

    @Given("a project with the name {string} is already created")
    public void projectWithNameAlreadyCreated(String name) {
        currentProject = projectSystem.getProjectByName(name);
        if (currentProject == null) {
            currentProject = projectSystem.createProject(name, LocalDate.now(), LocalDate.now().plusDays(30), 100);
        }
    }

    @And("project manager is logged in as employee {string}")
    public void projectManagerIsLoggedInAsEmployee(String id) {
        currentEmployee = projectSystem.getEmployeeById(id);
        if (currentEmployee == null) {
            currentEmployee = new Employee(id);
            projectSystem.registerEmployee(id);
        }
        currentEmployee.logIn();
    }

    @When("a new activity with name {string}, start date {string} and end date {string} is added to the project {string}")
    public void addActivityWithoutId(String name, String start, String end, String projectName) {
        LocalDate startDate = parse(start);
        LocalDate endDate = parse(end);

        if (startDate.isAfter(endDate)) {
            errorMessage = "End date cannot be before start date";
            return;
        }

        Activity activity = new Activity(
                projectSystem.generateActivityID(),
                name,
                startDate,
                endDate
        );

        try {
            projectSystem.getProjectByName(projectName).addActivity(activity);
        } catch (IllegalArgumentException ex) {
            errorMessage = ex.getMessage();
        }
    }


    @Then("the activity {string} should now be part of the project {string}")
    public void verifyActivityExists(String name, String projectName) {
        Project project = projectSystem.getProjectByName(projectName);
        Activity activity = project.getActivityByName(name);
        assertNotNull(activity, "Activity should exist in project");
    }


    @Then("the system should display the error {string}")
    public void systemDisplaysError(String expected) {
        assertEquals(expected, errorMessage);
    }

    @Given("a second project with the name {string} is created")
    public void createSecondProject(String name) {
        if (projectSystem.getProjectByName(name) == null) {
            projectSystem.createProject(name, LocalDate.now(), LocalDate.now().plusDays(30), 100);
        }
    }

    @When("the project manager tries to add another activity with name {string}, start date {string} and end date {string} to project {string}")
    public void tryAddingDuplicateActivityWithoutId(String name, String start, String end, String projectName) {
        // parse dates
        LocalDate startDate = parse(start);
        LocalDate endDate   = parse(end);

        // build a new Activity
        Activity duplicate = new Activity(
                projectSystem.generateActivityID(),
                name,
                startDate,
                endDate
        );

        try {
            // attempt to add it to the project
            projectSystem
                    .getProjectByName(projectName)
                    .addActivity(duplicate);
        } catch (IllegalArgumentException e) {
            // capture the exact “duplicate name” message
            errorMessage = e.getMessage();
        }
    }


    @Then("both projects should contain an activity named {string}")
    public void verifyActivityInBothProjects(String activityName) {
        Project a = projectSystem.getProjectByName("ProjectA");
        Project b = projectSystem.getProjectByName("ProjectB");

        assertNotNull(a.getActivityByName(activityName));
        assertNotNull(b.getActivityByName(activityName));
    }

    @When("employee {string} is assigned to the activity {string}")
    public void assignEmployeeToActivity(String id, String activityName) {
        Activity activity = currentProject.getActivityByName(activityName);
        Employee emp = projectSystem.getEmployeeById(id);
        try {
            if (!activity.isEmployeeAssigned(emp)) {
                activity.assignEmployee(emp);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();

        }
    }

    @Then("employee {string} should appear on the list of assigned employees for {string}")
    public void verifyEmployeeAssigned(String id, String activityName) {
        Activity activity = currentProject.getActivityByName(activityName);
        Employee emp = projectSystem.getEmployeeById(id);
        assertTrue(activity.isEmployeeAssigned(emp));
    }

    @Given("employee {string} is logged in and already assigned to 10 activities")
    public void assign10Activities(String id) {
        if (!projectSystem.isRegistered(id)) {
            projectSystem.registerEmployee(id);
        }

        currentEmployee = projectSystem.getEmployeeById(id);  // <-- kritisk linje!
        currentEmployee.logIn();

        for (int i = 0; i < 10; i++) {
            Activity dummy = new Activity("A" + i, "Dummy" + i, LocalDate.now(), LocalDate.now().plusDays(5));
            dummy.assignEmployee(currentEmployee);
        }

        projectSystem.addEmployee(id, currentProject.getName());
    }


    @When("the project manager attempts to assign employee {string} to a new activity with ID {string}, name {string}, start date {string} and end date {string}")
    public void failAssignmentDueToLimit(String id, String aid, String name, String start, String end) {
        try {
            Activity newActivity = new Activity(aid, name, parse(start), parse(end));
            newActivity.assignEmployee(currentEmployee);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    @And("employee {string} is already assigned to the activity {string}")
    public void employeeAlreadyAssigned(String id, String name) {
        Activity activity = currentProject.getActivityByName(name);
        Employee emp = projectSystem.getEmployeeById(id);
        if (!activity.isEmployeeAssigned(emp)) {
            activity.assignEmployee(emp);
        }
    }

    @When("the project manager tries to assign employee {string} to {string} again")
    public void assignEmployeeAgain(String id, String name) {
        assignEmployeeToActivity(id, name);
    }

    @And("another activity with name {string}, start date {string} and end date {string} is added to the project {string}")
    public void addSecondActivityWithoutId(String name, String start, String end, String projectName) {
        addActivityWithoutId(name, start, end, projectName);
    }

    @When("the project manager attempts to assign employee {string} to a new activity with name {string}, start date {string} and end date {string}")
    public void failAssignmentDueToLimitNoId(String empId, String name, String start, String end) {
        try {
            Employee emp = projectSystem.getEmployeeById(empId);
            if (emp == null) {
                emp = new Employee(empId);
                emp.logIn();
                projectSystem.registerEmployee(empId);
            }

            LocalDate startDate = parse(start);
            LocalDate endDate = parse(end);
            String generatedId = projectSystem.generateActivityID();

            Activity newActivity = new Activity(generatedId, name, startDate, endDate);
            currentProject.addActivity(newActivity);
            newActivity.assignEmployee(emp);
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }



}
