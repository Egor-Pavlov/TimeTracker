package org.example.timetracker.Scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.timetracker.Service.TimeTrackerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Class for scheduled task "cleanup"
 */
@Component
public class DataCleanUp {
    private final TimeTrackerService timeTrackerService;
    private static final Logger schedulerLogger = LogManager.getLogger("Scheduler");

    @Value("${data.retention.period.days}")
    private int retentionPeriodDays;

    /**
     * Constructor for cleanup task
     * @param timeTrackerService service to manage time tracking
     */
    public DataCleanUp(TimeTrackerService timeTrackerService) {
        this.timeTrackerService = timeTrackerService;
    }

    /**
     * Scheduled task to cleanup traking entries older than N days
     */
    @Scheduled(cron = "${scheduled.task.cron}")
    public void cleanUpOldData() {
        schedulerLogger.info("Starting cleanup task. Retention period: " + retentionPeriodDays + " days");
        int deletedRecords = timeTrackerService.cleanUpOldData(retentionPeriodDays);
        schedulerLogger.info("Cleanup task completed. Number of records deleted: " + deletedRecords);
    }
}
