package org.example.timetracker.Models;

import java.sql.Timestamp;
public class TimeEntry {
    private long id;
    private long user_Id;
    private long task_Id;
    private Timestamp startTime;
    private Timestamp endTime;
    private double duration;

    public TimeEntry(long id,
                     long user_Id,
                     long task_Id,
                     Timestamp startTime,
                     Timestamp endTime,
                     double duration)
    {
        this.id = id;
        this.user_Id = user_Id;
        this.task_Id = task_Id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public TimeEntry() {

    }

    public long getId() {
        return id;
    }

    public long getUser_id() {
        return user_Id;
    }

    public void setUser_id(long user_Id) {
        this.user_Id = user_Id;
    }

    public long getTask_id() {
        return task_Id;
    }

    public void setTask_id(long user_Id) {
        this.user_Id = user_Id;
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
        this.duration = this.endTime.getTime() - this.startTime.getTime();
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
