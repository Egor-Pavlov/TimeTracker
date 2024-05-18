package org.example.timetracker.Logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.example.timetracker.Controller.TimeTrackerRestController;
import org.example.timetracker.Service.TimeTrackerService;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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

Этот подход сокращает количество методов в классе и упрощает код, сохраняя при этом всю функциональность логирования.
 */
    private static final Logger serviceLogger = LogManager.getLogger(TimeTrackerService.class);
    private static final Logger controllerLogger = LogManager.getLogger(TimeTrackerRestController.class);

    @Pointcut("execution(* org.example.timetracker.Service..*(..))")
    public void serviceMethods() {
        // Pointcut для методов в пакетах Service
    }

    @Pointcut("execution(* org.example.timetracker.Controller..*(..))")
    public void controllerMethods() {
        // Pointcut для методов в пакетах Controller
    }

    @Before("serviceMethods() || controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        Logger logger = getLogger(joinPoint);
        logger.debug("Entering method: " + joinPoint.getSignature().getName() + " with arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceMethods() || controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        Logger logger = getLogger(joinPoint);
        logger.debug("Method " + joinPoint.getSignature().getName() + " executed successfully. Returned: " + result);
    }

    @AfterThrowing(pointcut = "serviceMethods() || controllerMethods()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        Logger logger = getLogger(joinPoint);
        logger.error("Method " + joinPoint.getSignature().getName() + " threw an exception: " + error.getMessage());
    }

    private Logger getLogger(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        if (className.startsWith("org.example.timetracker.Repositories")) {
            return serviceLogger;
        } else if (className.startsWith("org.example.timetracker.Controller")) {
            return controllerLogger;
        } else {
            return LogManager.getLogger(className);
        }
    }
}
