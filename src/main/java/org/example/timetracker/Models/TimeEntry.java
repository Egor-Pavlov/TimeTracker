package org.example.timetracker.Models;

import java.sql.Timestamp;

/**
 * Represents a time entry for tracking time spent on a task.
 */
public class TimeEntry {
    private long id;
    private long userId;
    private long taskId;
    private Timestamp startTime;
    private Timestamp endTime;
    private double duration;

    /**
     * Constructs a TimeEntry object with the specified parameters.
     * @param id The unique identifier of the time entry.
     * @param userId The user ID associated with the time entry.
     * @param taskId The task ID associated with the time entry.
     * @param startTime The start time of the time entry.
     * @param endTime The end time of the time entry.
     * @param duration The duration of the time entry in hours.
     */
    public TimeEntry(long id,
                     long userId,
                     long taskId,
                     Timestamp startTime,
                     Timestamp endTime,
                     double duration){
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    /**
     * Default constructor for creating an empty TimeEntry object.
     */
    public TimeEntry() {}

    /**
     * Returns the ID of the time entry.
     * @return The ID of the time entry.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the time entry.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the user ID associated with the time entry.
     * @return The user ID.
     */
    public long getuserId() {
        return userId;
    }

    /**
     * Sets the user ID for the time entry.
     * @param userId The user ID to set.
     */
    public void setuserId(long userId) {
        this.userId = userId;
    }

    /**
     * Returns the task ID associated with the time entry.
     * @return The task ID.
     */
    public long gettaskId() {
        return taskId;
    }

    /**
     * Sets the task ID for the time entry.
     * @param taskId The task ID to set.
     */
    public void settaskId(long taskId) {
        this.taskId = taskId;
    }

    /**
     * Returns the start time of the time entry.
     * @return The start time.
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
    * Sets the start time for the time entry.
    * @param startTime The start time to set.
    */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the end time of the time entry.
     * @return The end time.
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time for the time entry and calculates the duration.
     * @param endTime The end time to set.
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        this.endTime.setNanos(0);
        this.duration =  (double)(this.endTime.getTime() - this.startTime.getTime())/ (1000 * 60 * 60);
        this.duration = Math.round(this.duration * 100.0) / 100.0;
    }

    /**
     * Returns the duration of the time entry in hours.
     * @return The duration in hours.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Sets the duration for the time entry.
     * @param duration The duration to set.
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Returns a string representation of the TimeEntry object.
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "TimeEntry{" +
                "id=" + id +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                '}';
    }
}