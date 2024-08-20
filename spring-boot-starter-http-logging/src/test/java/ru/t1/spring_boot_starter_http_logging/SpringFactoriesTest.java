package ru.t1.spring_boot_starter_http_logging;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpringFactoriesTest {

    @Test
    void shouldContainHttpLoggingAutoConfiguration() {
        List<String> autoConfigurationClasses = SpringFactoriesLoader.loadFactoryNames(
                EnableAutoConfiguration.class, getClass().getClassLoader());

        assertThat(autoConfigurationClasses)
                .contains("ru.t1.spring_boot_starter_http_logging.logger.HttpLoggingAutoConfiguration");
    }
}
