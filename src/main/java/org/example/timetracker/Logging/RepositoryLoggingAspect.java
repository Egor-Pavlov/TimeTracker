package org.example.timetracker.Logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.TimeEntriesRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("repositoryLoggingAspect")
@Aspect
public class RepositoryLoggingAspect {
    private static final Logger taskRepositoryLogger = LogManager.getLogger(TasksRepository.class);
    private static final Logger userRepositoryLogger = LogManager.getLogger(UsersRepository.class);
    private static final Logger timeEntryRepositoryLogger = LogManager.getLogger(TimeEntriesRepository.class);

    @Pointcut("execution(* org.example.timetracker.Repositories.TasksRepository.*(..))")
    public void tasksRepositoryMethods() {
        // Pointcut для методов в пакетах Repositories
    }
    @Pointcut("execution(* org.example.timetracker.Repositories.UsersRepository.*(..))")
    public void usersRepositoryMethods() {
        // Pointcut для методов в пакетах Repositories
    }
    @Pointcut("execution(* org.example.timetracker.Repositories.TimeEntriesRepository.*(..))")
    public void timeEntryRepositoryMethods() {
        // Pointcut для методов в пакетах Repositories
    }

    @Before("tasksRepositoryMethods() || usersRepositoryMethods() || timeEntryRepositoryMethods()")
    public void logBefore(JoinPoint joinPoint) {
        Logger logger = getLogger(joinPoint);
        logger.debug("Entering method: " + joinPoint.getSignature().getName() + " with arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "tasksRepositoryMethods() || usersRepositoryMethods() || timeEntryRepositoryMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Logger logger = getLogger(joinPoint);
        logger.debug("Method " + joinPoint.getSignature().getName() + " executed successfully. Returned: " + result);
    }

    @AfterThrowing(pointcut = "tasksRepositoryMethods() || usersRepositoryMethods() || timeEntryRepositoryMethods()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        Logger logger = getLogger(joinPoint);
        logger.error("Method " + joinPoint.getSignature().getName() + " threw an exception: " + error.getMessage());
    }

    private Logger getLogger(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        if (className.endsWith("TasksRepository")) {
            return taskRepositoryLogger;
        } else if (className.endsWith("UsersRepository")) {
            return userRepositoryLogger;
        } else if (className.endsWith("TimeEntryRepository")) {
            return timeEntryRepositoryLogger;
        } else {
            return LogManager.getLogger(className);
        }
    }
}

