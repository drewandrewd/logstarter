package com.example.logstarter.aspect;

import com.example.logstarter.annotation.LogExecution;
import com.example.logstarter.properties.LogStarterProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * Аспект для логирования методов, помеченных аннотацией {@link LogExecution}.
 */
@Slf4j
@Aspect
public class LoggingAspect {

    private final LogStarterProperties props;

    public LoggingAspect(LogStarterProperties props) {
        this.props = props;
    }

    /**
     * Логирует информацию перед выполнением метода.
     *
     * @param joinPoint точка соединения с деталями вызываемого метода
     */
    @Before("@annotation(com.example.logstarter.annotation.LogExecution)")
    public void logBefore(JoinPoint joinPoint) {
        String message = "Before method: %s with args %s".formatted(joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        logByLevel(message);
    }

    /**
     * Логирует результат после успешного выполнения метода.
     *
     * @param joinPoint точка соединения
     * @param result возвращаемое значение метода
     */
    @AfterReturning(pointcut = "@annotation(com.example.logstarter.annotation.LogExecution)",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String message = "AfterReturning from method: %s with result: %s".formatted(joinPoint.getSignature().getName(), result);
        logByLevel(message);
    }

    /**
     * Логирует исключение, если метод выбрасывает ошибку.
     *
     * @param joinPoint точка соединения
     * @param exception исключение, выброшенное методом
     */
    @AfterThrowing(pointcut = "@annotation(com.example.logstarter.annotation.LogExecution)",
            throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception thrown in method: {} with message: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage(),
                exception);
    }

    /**
     * Логирует время выполнения метода.
     *
     * @param joinPoint точка соединения
     * @return результат выполнения метода
     * @throws Throwable проброс исключений из исходного метода
     */
    @LogExecution
    @Around("@annotation(com.example.logstarter.annotation.LogExecution)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            String message = "Around method: %s executed in %s ms".formatted(joinPoint.getSignature().getName(), elapsedTime);
            logByLevel(message);
            return result;
        } catch (Throwable ex) {
            log.error("Exception in Around advice: {}", ex.getMessage());
            throw ex;
        }
    }

    /**
     * Логирует сообщение с уровнем, указанным в настройках {@link LogStarterProperties}.
     *
     * @param message текст лог-сообщения
     */
    private void logByLevel(String message) {
        switch (props.getLevel()) {
            case TRACE -> log.trace(message);
            case DEBUG -> log.debug(message);
            case INFO -> log.info(message);
            case WARN -> log.warn(message);
            case ERROR -> log.error(message);
        }
    }
}