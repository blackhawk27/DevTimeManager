package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeJUnitTest {
    private Employee emp;
    private ProjectSystem projSys;
    private Project proj;

    @BeforeEach
    void init() {
        emp = new Employee("huba");
        projSys = new ProjectSystem();
        proj = new Project("Project1", "25001", LocalDate.now(), LocalDate.now().plusDays(1), 120);
    }

    @Test
    void loginShouldSetLoggedInTrue() {
        // initially not logged in
        assertFalse(emp.isLoggedIn());
        emp.logIn();
        assertTrue(emp.isLoggedIn());
    }

    @Test
    void logoutShouldSetLoggedInFalse() {
        emp.logIn();
        assertTrue(emp.isLoggedIn());
        emp.logOut();
        assertFalse(emp.isLoggedIn());
    }

    @Test
    void cannotCreateWithEmptyName() { // to test empty and not only null
        emp.logIn();
        emp.inputProjectName("");
        emp.inputStartDate(LocalDate.now());
        emp.inputEndDate(LocalDate.now().plusDays(1));
        emp.inputBudgetedTime(10);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> emp.createProject(projSys)
        );
        assertEquals("Project name missing. Project has not been created.", ex.getMessage());
    }
}
