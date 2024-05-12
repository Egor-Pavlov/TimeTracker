package org.example.timetracker.DTO;

public class TimeEntryDTO {
    private long user_id;
    private long task_id;

    public TimeEntryDTO(long user_id, long task_id) {
        this.user_id = user_id;
        this.task_id = task_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }
}
