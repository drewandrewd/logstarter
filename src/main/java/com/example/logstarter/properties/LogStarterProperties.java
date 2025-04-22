package com.example.logstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Конфигурационные свойства логгера LogStarter.
 * <p>
 * Используются для управления поведением логирования через {@code application.yml}.
 * <p>
 */
@ConfigurationProperties(prefix = "logstarter")
public class LogStarterProperties {

    private boolean enabled = true;

    private LogLevel level = LogLevel.INFO;

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }
}
