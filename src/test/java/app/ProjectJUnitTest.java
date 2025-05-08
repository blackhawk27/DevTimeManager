package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
}