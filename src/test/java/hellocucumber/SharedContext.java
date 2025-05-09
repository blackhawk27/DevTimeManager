package hellocucumber;

import app.ProjectSystem;

public class SharedContext {
    public static ProjectSystem projectSystem;

    static {
        projectSystem = new ProjectSystem(); // initial value
    }
}