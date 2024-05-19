package org.example.timetracker.Logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


@Component("repositoryLoggingAspect")
@Aspect
public class RepositoryLoggingAspect {
    private static final Logger repositoryLogger = LogManager.getLogger("Database");

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
        repositoryLogger.debug("Entering method: " + joinPoint.getSignature().getName() + " with arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "tasksRepositoryMethods() || usersRepositoryMethods() || timeEntryRepositoryMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {

        repositoryLogger.debug("Method " + joinPoint.getSignature().getName() + " executed successfully. Returned: " +  ObjectToString.objectToString(result) );
    }

    @AfterThrowing(pointcut = "tasksRepositoryMethods() || usersRepositoryMethods() || timeEntryRepositoryMethods()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        repositoryLogger.error("Method " + joinPoint.getSignature().getName() + " threw an exception: " + error.getMessage());
    }

}

