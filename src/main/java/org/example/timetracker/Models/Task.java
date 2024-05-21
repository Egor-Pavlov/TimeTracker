package org.example.timetracker.Models;

import java.sql.Timestamp;

/**
 * The Task class represents a task with a unique task ID, theme, description, and creation date.
 */
public class Task {
    private final long taskID;
    private String theme;
    private String description;
    private Timestamp creationDate;

    /**
     * Constructs a Task object with the given task ID, theme, description, and creation date.
     * @param taskID the unique task ID
     * @param theme the theme of the task
     * @param description the description of the task
     * @param creationDate the creation date of the task
     * @throws IllegalArgumentException if taskID is negative
     */
    public Task(long taskID, String theme, String description, Timestamp creationDate) {
        this.creationDate = creationDate;
        if (taskID < 0)
            throw new IllegalArgumentException("Task ID cannot be negative");

        this.taskID = taskID;
        setTaskTheme(theme);
        setTaskDescription(description);
    }

    /**
     * Returns the task ID.
     * @return the task ID
     */
    public long getTaskID() {
        return taskID;
    }

    /**
     * Returns the theme of the task.
     * @return the theme of the task
     */
    public String getTaskTheme() {
        return theme;
    }

    /**
     * Sets the theme of the task.
     * @param taskTheme the theme of the task
     * @throws IllegalArgumentException if the theme is null or empty
     */
    public void setTaskTheme(String taskTheme) throws IllegalArgumentException {
        if (taskTheme == null || taskTheme.isEmpty())
            throw new IllegalArgumentException("taskTheme cannot be null or empty");
        this.theme = taskTheme;
    }

    /**
     * Returns the description of the task.
     * @return the description of the task
     */
    public String getTaskDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     * @param taskDescription the description of the task
     * @throws IllegalArgumentException if the description is null or empty
     */
    public void setTaskDescription(String taskDescription) throws IllegalArgumentException {
        if (taskDescription == null || taskDescription.isEmpty())
            throw new IllegalArgumentException("taskDescription cannot be null or empty");
        this.description = taskDescription;
    }

    /**
     * Returns the creation date of the task.
     * @return the creation date of the task
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the task.
     * @param creationDate the creation date of the task
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Returns a string representation of the Task object.
     * @return a string representation of the Task object
     */
    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", theme='" + theme + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}