package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeJUnitTest {
    private Employee emp;
    private ProjectSystem projSys;
    private Project proj;
    private Activity act; // Deklarér 'act' som en instansvariabel

    @BeforeEach
    void init() {
        projSys = new ProjectSystem();
        projSys.registerEmployee("E1");
        emp = projSys.getEmployeeById("E1");

        // Opret projekt
        proj = projSys.createProject("P1", LocalDate.now(), LocalDate.now().plusDays(5), 50.0);

        // Opret aktivitet og tilføj til projektet
        act = new Activity("A1", "Development", LocalDate.now(), LocalDate.now().plusDays(5));
        proj.addActivity(act);

        // Tildel medarbejderen til projektet
        projSys.addEmployee("E1", "P1");
    }




    @Test
    void loginShouldSetLoggedInTrue() {
        emp.logOut();  // Sikrer at medarbejderen starter som ikke logget ind
        assertFalse(emp.isLoggedIn());
        emp.logIn();
        assertTrue(emp.isLoggedIn());
    }


    @Test
    void logoutShouldSetLoggedInFalse() {
        emp.logIn();
        emp.logOut();
        assertFalse(emp.isLoggedIn());
    }

    @Test
    void cannotCreateProjectWithoutLogin() {
        emp.inputProjectName("New Project");
        emp.inputStartDate(LocalDate.now());
        emp.inputEndDate(LocalDate.now().plusDays(1));
        emp.inputBudgetedTime(20.0);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> emp.createProject(projSys)
        );
        assertEquals("Employee must be logged in to create a project.", ex.getMessage());
    }


    @Test
    void registerWorkTimeSuccessfully() {
        emp.logIn();
        ArrayList<String> workTime = new ArrayList<>();
        workTime.add("08/05/2025-09:00");
        workTime.add("08/05/2025-12:00");

        String response = emp.registerTime("Work", workTime, "P1", "A1", projSys);
        assertEquals("Registration completed", response);
    }

    @Test
    void registerTimeForUnassignedActivity() {
        emp.logIn();

        // Opret nyt projekt gennem ProjectSystem
        Project newProj = projSys.createProject("P2", LocalDate.now(), LocalDate.now().plusDays(1), 20.0);

        // Opretter aktivitet "Testing" og tilføjer den til projektet
        Activity unassignedAct = new Activity("A2", "Testing", LocalDate.now(), LocalDate.now().plusDays(1));
        newProj.addActivity(unassignedAct);

        // Vi tilføjer **ikke** medarbejderen til aktiviteten

        ArrayList<String> workTime = new ArrayList<>();
        workTime.add("08/05/2025-10:00");
        workTime.add("08/05/2025-12:00");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> emp.registerTime("Work", workTime, "P2", "Testing", projSys)
        );
        assertEquals("Employee is not assigned to the activity", ex.getMessage());
    }


    @Test
    void registerFreeTimeWithoutOverlap() {
        emp.logIn();
        ArrayList<String> freeTime = new ArrayList<>();
        freeTime.add("08/05/2025");
        freeTime.add("08/05/2025");

        String response = emp.registerTime("FreeTime", freeTime, "", "", projSys);
        assertEquals("Absence registered", response);
    }
}
