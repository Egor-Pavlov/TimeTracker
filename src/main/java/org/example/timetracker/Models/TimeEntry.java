package org.example.timetracker.Models;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

public class TimeEntry {
   private long id;
   private long user_id;
   private long task_id;
   private DateTimeLiteralExpression.DateTime startTime;
   private DateTimeLiteralExpression.DateTime endTime;
   private double duration;

    public TimeEntry(long id,
                     long user_id,
                     long task_id,
                     DateTimeLiteralExpression.DateTime startTime,
                     DateTimeLiteralExpression.DateTime endTime,
                     double duration)
    {
        this.id = id;
        this.user_id = user_id;
        this.task_id = task_id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public long getId() {
        return id;
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

    public DateTimeLiteralExpression.DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTimeLiteralExpression.DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTimeLiteralExpression.DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTimeLiteralExpression.DateTime endTime) {
        this.endTime = endTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
