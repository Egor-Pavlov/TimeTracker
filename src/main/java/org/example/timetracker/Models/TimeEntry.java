package org.example.timetracker.Models;

import java.sql.Timestamp;
public class TimeEntry {
    private long id;
    private long userId;
    private long taskId;
    private Timestamp startTime;
    private Timestamp endTime;
    private double duration;

    public TimeEntry(long id,
                     long userId,
                     long taskId,
                     Timestamp startTime,
                     Timestamp endTime,
                     double duration)
    {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public TimeEntry() {

    }

    public long getId() {
        return id;
    }

    public long getuserId() {
        return userId;
    }

    public void setuserId(long userId) {
        this.userId = userId;
    }

    public long gettaskId() {
        return taskId;
    }

    public void settaskId(long userId) {
        this.userId = userId;
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
        this.endTime.setNanos(0);
        this.duration =  (double)(this.endTime.getTime() - this.startTime.getTime())/ (1000 * 60 * 60);
        this.duration = Math.round(this.duration * 100.0) / 100.0;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
