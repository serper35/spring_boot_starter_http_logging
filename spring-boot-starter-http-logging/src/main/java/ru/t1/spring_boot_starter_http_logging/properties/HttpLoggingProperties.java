package ru.t1.spring_boot_starter_http_logging.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;




@Data
@Getter
@Setter
@ConfigurationProperties(prefix = "http.logging")
@Validated
public class HttpLoggingProperties {
    private Level level = Level.INFO;
    private boolean enabled = true;
}
