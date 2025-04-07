package app;

import java.time.LocalDate;

public class Project {
    private String name;
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;

    public Project(String name, String id, LocalDate start, LocalDate end) {
        this.name = name;
        this.id = id;
        this.startDate = start;
        this.endDate = end;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}

