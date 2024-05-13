package org.example.timetracker.DTO;

public class TimeEntryRequest {
    private long userId;
    private long taskId;

    public TimeEntryRequest(long userId, long taskId) {
        this.userId = userId;
        this.taskId = taskId;
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
}
