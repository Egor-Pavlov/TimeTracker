package org.example.timetracker.Scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StopTrackingConfig {

    @Value("${scheduled.task.cron}")
    private String cronExpression;

    public String getCronExpression() {
        return cronExpression;
    }
}
