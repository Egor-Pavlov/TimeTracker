package org.example.timetracker.Logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for logging method calls in Service and Controller classes.
 */
@Aspect
@Component
public class LoggingAspect {
/*
Универсальные методы логирования:
    Созданы общие методы logBefore, logAfterReturning, и logAfterThrowing,
    которые работают как для сервисов, так и для контроллеров.
Динамическое определение логгера:
    Метод getLogger определяет, какой логгер использовать, исходя из пакета класса,
    к которому относится вызываемый метод.
Объединенные Pointcut-ы:
    В аннотациях @Before, @AfterReturning и @AfterThrowing объединены два Pointcut-а
    serviceMethods() и controllerMethods() с помощью оператора ||.

 */
    /**
     * Logger for Service methods.
     */
    private static final Logger serviceLogger = LogManager.getLogger("TimeTrackerService");

    /**
     * Logger for Controller methods.
     */
    private static final Logger controllerLogger = LogManager.getLogger("RestController");

    /**
     * Defines Pointcut for methods in the Service package.
     */
    @Pointcut("execution(* org.example.timetracker.Service..*(..))")
    public void serviceMethods() {
        // Pointcut для методов в пакетах Service
    }

    /**
     * Defines Pointcut for methods in the Controller package.
     */
    @Pointcut("execution(* org.example.timetracker.Controller..*(..))")
    public void controllerMethods() {
        // Pointcut для методов в пакетах Controller
    }

    /**
     * Logs before method execution for Service and Controller methods.
     * @param joinPoint the JoinPoint before method execution
     */
    @Before("serviceMethods() || controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        Logger logger = getLogger(joinPoint);
        logger.debug("Entering method: " + joinPoint.getSignature().getName() + " with arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * Logs after method execution for Service and Controller methods.
     * @param joinPoint the JoinPoint after method execution
     * @param result the result returned by the method
     */
    @AfterReturning(pointcut = "serviceMethods() || controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Logger logger = getLogger(joinPoint);
        logger.debug("Method " + joinPoint.getSignature().getName() + " executed successfully. Returned: " + ObjectToString.objectToString(result));
    }

    /**
     * Logs after throwing an exception in Service and Controller methods.
     * @param joinPoint the JoinPoint after method execution
     * @param error the exception thrown by the method
     */
    @AfterThrowing(pointcut = "serviceMethods() || controllerMethods()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        Logger logger = getLogger(joinPoint);
        logger.error("Method " + joinPoint.getSignature().getName() + " threw an exception: " + error.getMessage());
    }

    /**
     * Determines the appropriate logger based on the class package.
     * @param joinPoint the JoinPoint for the method
     * @return the logger instance
     */
    private Logger getLogger(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        if (className.startsWith("org.example.timetracker.Service")) {
            return serviceLogger;
        } else if (className.startsWith("org.example.timetracker.Controller")) {
            return controllerLogger;
        } else {
            return LogManager.getLogger(className);
        }
    }
}
