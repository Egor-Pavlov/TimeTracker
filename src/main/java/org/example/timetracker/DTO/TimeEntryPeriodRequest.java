package org.example.timetracker.DTO;

import java.sql.Timestamp;

public class TimeEntryPeriodRequest {
    private long userId;
    private long taskId;
    private Timestamp startTime;
    private Timestamp endTime;

    public TimeEntryPeriodRequest(long userId, long taskId, Timestamp startTime, Timestamp endTime) {
        this.userId = userId;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getUser_id() {
        return userId;
    }

    public void setUser_id(long user_id) {
        this.userId = user_id;
    }

    public long getTask_id() {
        return taskId;
    }

    public void setTask_id(long task_id) {
        this.taskId = task_id;
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
