package org.example.timetracker.Scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.timetracker.Service.TimeTrackerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StopTracking {
    private final TimeTrackerService timeTrackerService;
    private static final Logger schedulerLogger = LogManager.getLogger("Scheduler");
    @Value("${scheduled.task.cron}")
    private String cronExpression;

    public StopTracking(TimeTrackerService timeTrackerService) {
        this.timeTrackerService = timeTrackerService;
    }

    @Scheduled(cron = "${scheduled.task.cron}")
    public void stopTracking() {
        schedulerLogger.info("Start scheduled task. Cron expression: " + cronExpression
                + "Stopping tracking for all unstopped tasks");
        if (timeTrackerService.stopTrackingAll()){
            schedulerLogger.info("Tracking stopped for all unstopped tasks");
        }
    }
}
