package ru.t1.spring_boot_starter_http_logging;

import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.TestPropertySource;
import ru.t1.spring_boot_starter_http_logging.properties.HttpLoggingProperties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableConfigurationProperties(HttpLoggingProperties.class)
@TestPropertySource(properties = {
        "http.logging.enabled=true",
        "http.logging.level=DEBUG"
})
class HttpLoggingPropertiesTest {

    @Autowired
    private HttpLoggingProperties httpLoggingProperties;

    @Test
    void testPropertiesBinding() {
        // Проверяем, что свойства корректно привязаны
        assertThat(httpLoggingProperties.isEnabled()).isTrue();
        assertThat(httpLoggingProperties.getLevel()).isEqualTo(Level.DEBUG);
    }

    @Test
    void testDefaultValues() {
        new ApplicationContextRunner()
                .withUserConfiguration(HttpLoggingProperties.class)
                .run(context -> {
                    HttpLoggingProperties properties = context.getBean(HttpLoggingProperties.class);
                    assertThat(properties.isEnabled()).isTrue();
                    assertThat(properties.getLevel()).isEqualTo(Level.INFO);
                });
    }

    @Test
    void testInvalidLogLevel() {
        new ApplicationContextRunner()
                .withPropertyValues("http.logging.level=INVALID")
                .withUserConfiguration(HttpLoggingProperties.class)
                .run(context -> {
                    HttpLoggingProperties properties = context.getBean(HttpLoggingProperties.class);
                    assertThat(properties.getLevel()).isEqualTo(Level.INFO);
                });
    }

    @Test
    void testMissingEnabledProperty() {
        new ApplicationContextRunner()
                .withPropertyValues("http.logging.level=DEBUG")
                .withUserConfiguration(HttpLoggingProperties.class)
                .run(context -> {
                    HttpLoggingProperties properties = context.getBean(HttpLoggingProperties.class);
                    assertThat(properties.isEnabled()).isTrue();
                });
    }
}



