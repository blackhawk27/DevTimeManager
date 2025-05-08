package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProjectSystemJUnitTest {
    private ProjectSystem system;
    private Project project;
    private Employee emp;

    @BeforeEach
    void setUp() {
        system  = new ProjectSystem();
        system.registerEmployee("huba");
        emp      = system.getEmployeeById("huba");
        project  = system.createProject("New Website", LocalDate.now(), LocalDate.now().plusDays(10), 120);
    }

    @Test
    void addEmployeeSuccessfully() {
        system.addEmployee("huba", "New Website");
        assertTrue(project.getEmployees().contains(emp),
                "Employee should have been added to the project");
    }

    @Test
    void addingSameEmployeeTwiceIsNoOp() {
        system.addEmployee("huba", "New Website");
        system.addEmployee("huba", "New Website");
        assertEquals(1, project.getEmployees().size(),
                "Duplicate add should not increase list size");
    }

    @Test
    void cannotAddUnregisteredEmployee() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> system.addEmployee("X99", "New Website")
        );
        assertEquals("Employee with ID X99 does not exist", ex.getMessage());
    }

    @Test
    void cannotAddToNonexistentProject() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> system.addEmployee("huba", "NoSuchProj")
        );
        assertEquals("Project with ID NoSuchProj does not exist", ex.getMessage());
    }

    @Test
    void addEmployeeToActivitySuccessfully() {
        system.addEmployee("huba", "New Website");
        String aid = system.generateActivityID();
        Activity act = new Activity(aid, "Design",
                LocalDate.now(), LocalDate.now().plusDays(5));
        project.addActivity(act);
        system.addEmployee("huba", "New Website", "Design");
        assertTrue(act.isEmployeeAssigned(emp),
                "Employee should be assigned to the activity");
    }

    @Test
    void cannotAddToMissingActivity() {
        system.addEmployee("huba", "New Website");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> system.addEmployee("huba", "New Website", "NoAct")
        );
        assertEquals("Activity NoAct not found in project New Website", ex.getMessage());
    }

    @Test
    void getProjectByName_null_throws() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> system.getProjectByName(null)
        );
        assertEquals("Project name cannot be null or empty", ex.getMessage());
    }

    @Test
    void getProjectByName_empty_throws() {
        assertThrows(
                IllegalArgumentException.class,
                () -> system.getProjectByName("   ")
        );
    }

    @Test
    void removeProjectByName_removesProject() {
        assertNotNull(system.getProjectByName("New Website"));

        system.removeProjectByName("New Website");
        assertNull(system.getProjectByName("New Website"),
                "Project should have been removed by removeProjectByName");
    }
}
