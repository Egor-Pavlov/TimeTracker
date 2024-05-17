package org.example.timetracker.DTO;

import java.sql.Timestamp;

public class WorkInterval {
    private final long taskID;
    private String theme;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;

    public WorkInterval(long taskID, String theme, String description, Timestamp startTime, Timestamp endTime) {
        this.taskID = taskID;
        this.theme = theme;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getTaskID() {
        return taskID;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
