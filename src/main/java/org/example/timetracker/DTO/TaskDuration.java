package org.example.timetracker.DTO;

public class TaskDuration {
    private Long task_ID;
    private Double total_DUR;
    public TaskDuration(Long task_ID, Double total_DUR) {
        this.task_ID = task_ID;
        this.total_DUR = total_DUR;
    }
    public Long getTaskId(){
        return task_ID;
    }
    public Double getTotalDuration(){
        return total_DUR;
    }
    public void setTotalDuration(Double total_DUR) {
        this.total_DUR = total_DUR;
    }

    @Override
    public String toString() {
        return "TaskDuration{" +
                "task_ID=" + task_ID +
                ", total_DUR=" + total_DUR +
                '}';
    }
}
