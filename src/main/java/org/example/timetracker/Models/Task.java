package org.example.timetracker.Models;

public class Task {
    private final long taskID;
    private String theme;
    private String description;

    public Task(long taskID, String theme, String description) {
        if (taskID < 0)
            throw new IllegalArgumentException("Task ID cannot be negative");

        this.taskID = taskID;
        setTaskTheme(theme);
        setTaskDescription(description);
    }
    public long getTaskID() {
        return taskID;
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
