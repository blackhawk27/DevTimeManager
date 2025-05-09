package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectJUnitTest {
    private Project p;
    private Employee e1;
    private ProjectManager pm;
    private Activity a;

    @BeforeEach
    void init() {
        p = new Project("X", "0001", LocalDate.now(), LocalDate.now().plusDays(1), 120.0);
        e1 = new Employee("huba");
        pm = new ProjectManager("huba");
        p.addEmployee(e1);
        a = new Activity("A001", "act", LocalDate.now(), LocalDate.now().plusDays(1));

    }

    @Test
    void assignManagerWhenNone() {
        p.assignProjectManager(e1, pm);
        assertSame(pm, p.getProjectManager());
        assertTrue(pm.getManagedProjects().contains(p));
    }

    @Test
    void cannotOverwriteManager() {
        p.assignProjectManager(e1, pm);
        Employee other = new Employee("teem");
        p.addEmployee(other);
        ProjectManager pm2 = new ProjectManager("teem");
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> p.assignProjectManager(other, pm2)
        );
        assertEquals("This project already has a project manager. You cannot assign a new one.", ex.getMessage());
    }

    @Test
    void getStartAndEndDates() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        assertEquals(today, p.getStartDate(), "startDate should match constructor");
        assertEquals(tomorrow, p.getEndDate(), "endDate   should match constructor");
    }

    @Test
    void getActivity(){
        p.addActivity(a);
        assertEquals(p.getActivities(), List.of(a));
    }

    @Test
    void getBudgetedTime() {
        // this is the new test for your budgetedTime field
        assertEquals(120, p.getBudgetedTime(), "budgetedTime should match constructor");
    }

    @Test
    void generateReportfailsWhenNameIsNull() {
        Project project = new Project(null, "P999", LocalDate.now(), LocalDate.now().plusDays(1), 10.0);
        project.setProjectManager(new ProjectManager("PM1"));
        assertThrows(AssertionError.class, project::generateReport);
    }

    @Test
    void generateReportwarnsWhenNoTimeRegistered() {
        Project project = new Project("EmptyTime", "P002", LocalDate.now(), LocalDate.now().plusDays(5), 10.0);
        project.setProjectManager(new ProjectManager("PM2"));
        project.addEmployee(new Employee("E002"));

        String report = project.generateReport();
        assertTrue(report.contains("No work has been registered"));
    }

    @Test
    void generateReport_shouldCoverLines68To184() {
        ProjectSystem ps = new ProjectSystem();
        Employee emp = new Employee("E001");
        emp.logIn();
        ps.registerEmployee("E001");

        // Create project
        Project project = ps.createProject("CyberAI", LocalDate.now(), LocalDate.now().plusDays(5), 40);
        project.addEmployee(emp);

        // Create and assign activity
        Activity act = new Activity("A100", "ML Model", LocalDate.now(), LocalDate.now().plusDays(5));
        project.addActivity(act);
        act.assignEmployee(emp);

        // Assign PM
        ProjectManager pm = new ProjectManager("PM1");
        project.setProjectManager(pm);

        // Prepare time data as strings
        ArrayList<String> time = new ArrayList<>();
        time.add(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "-08:00");
        time.add(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "-16:00");

        // Register time correctly using the method
        String result = emp.registerTime("Work", time, "CyberAI", "ML Model", ps);
        assertEquals("Registration completed", result);

        // Generate report to cover all branches
        String report = project.generateReport();

        assertTrue(report.contains("Project Report"));
        assertTrue(report.contains("Project ID:"));
        assertTrue(report.contains("ML Model"));
        assertTrue(report.contains("Project is within budget."));
        assertTrue(report.contains("Time Entries:"));
    }

    @Test
    void generateReport_shouldIncludeNonWorkEntryBranch() {
        ProjectSystem ps = new ProjectSystem();
        Employee emp = new Employee("E777");
        emp.logIn();
        ps.registerEmployee("E777");

        // Create and register project
        Project project = ps.createProject("FlexiAI", LocalDate.now(), LocalDate.now().plusDays(3), 10);
        project.addEmployee(emp);

        // Assign dummy project manager to pass getProjectManager().getId()
        ProjectManager pm = new ProjectManager("PMX");
        project.setProjectManager(pm);

        // Time entry of non-Work type
        ArrayList<String> dates = new ArrayList<>();
        dates.add("09/05/2025");
        dates.add("09/05/2025");

        // Register FreeTime to trigger the else-branch
        String result = emp.registerTime("FreeTime", dates, "FlexiAI", "", ps);
        assertEquals("Absence registered", result);

        String report = project.generateReport();

        assertTrue(report.contains("Start:"), "Start date line not included");
        assertTrue(report.contains("FreeTime"), "Type label not mentioned");
        assertTrue(report.contains("Project ID:"), "Project ID missing");
    }




}