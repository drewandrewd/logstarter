package com.example.logstarter.config;

import com.example.logstarter.aspect.LoggingAspect;
import com.example.logstarter.properties.LogStarterProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Автоконфигурация для стартер-модуля логирования.
 * <p>
 * Регистрирует аспект {@link LoggingAspect}, если включено свойство {@code logstarter.enabled=true}
 */
@Configuration
@EnableConfigurationProperties(LogStarterProperties.class)
@ConditionalOnProperty(prefix = "logstarter", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LogStarterAutoConfiguration {

    @Bean
    public LoggingAspect logAspect(LogStarterProperties props) {
        return new LoggingAspect(props);
    }
}
