package org.example.timetracker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimeTrackerApplication {
    private static final Logger logger = LogManager.getLogger(TimeTrackerApplication.class);
    public static void main(String[] args) {
        logger.error("Start Time-Tracker service.");
        SpringApplication.run(TimeTrackerApplication.class, args);
    }



}
