package org.example.timetracker.DTO;

public class TaskRequest {
    private String theme;
    private String description;

    public TaskRequest( String theme, String description) {
        setTaskTheme(theme);
        setTaskDescription(description);
    }

    public String getTaskTheme() {
        return theme;
    }
    public void setTaskTheme(String taskTheme) throws IllegalArgumentException {
        if (taskTheme == null || taskTheme.isEmpty())
            throw new IllegalArgumentException("taskTheme cannot be null or empty");
        this.theme = taskTheme;
    }
    public String getTaskDescription() {
        return description;
    }
    public void setTaskDescription(String taskDescription) throws IllegalArgumentException {
        if (taskDescription == null || taskDescription.isEmpty())
            throw new IllegalArgumentException("taskDescription cannot be null or empty");
        this.description = taskDescription;
    }
}
