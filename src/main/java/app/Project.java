package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private String name;
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private final List<Activity> activities = new ArrayList<>();

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public Activity getActivityByName(String activityName) {
        for (Activity activity : activities) {
            if (activity.getName().equals(activityName)) {
                return activity;
            }
        }
        return null;
    }

    public List<Activity> getActivities() {
        return new ArrayList<>(activities); // Return a copy to protect internal list
    }
}

