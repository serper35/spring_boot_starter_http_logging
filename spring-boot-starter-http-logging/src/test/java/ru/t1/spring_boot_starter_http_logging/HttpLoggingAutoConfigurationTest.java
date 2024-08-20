package ru.t1.spring_boot_starter_http_logging;

import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.t1.spring_boot_starter_http_logging.logger.HttpLoggingAutoConfiguration;
import ru.t1.spring_boot_starter_http_logging.logger.HttpLoggingInterceptor;
import ru.t1.spring_boot_starter_http_logging.properties.HttpLoggingProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class HttpLoggingAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(HttpLoggingAutoConfiguration.class);

    @Autowired
    private HttpLoggingAutoConfiguration httpLoggingAutoConfiguration;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HttpLoggingInterceptor interceptor;

    @Autowired
    private HttpLoggingProperties properties;

    @Test
    void shouldLoadHttpLoggingAutoConfiguration() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(HttpLoggingAutoConfiguration.class);
        });
    }

    @Test
    void testInterceptorIsRegistered() {
        assertNotNull(interceptor);
    }

    @Test
    void testPropertiesAreInjected() {
        assertNotNull(properties);
        assertTrue(properties.isEnabled());
        assertEquals(Level.INFO, properties.getLevel());
    }

    @Test
    void testHttpRequestLogging() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void testInterceptorBeanCreated() {
        this.contextRunner
                .withPropertyValues("http.logging.enabled=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(HttpLoggingInterceptor.class);
                    assertThat(context).hasSingleBean(WebMvcConfigurer.class);
                });
    }

    @Test
    void testInterceptorBeanNotCreatedWhenDisabled() {
        this.contextRunner
                .withPropertyValues("http.logging.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(HttpLoggingInterceptor.class);
                    assertThat(context).doesNotHaveBean(WebMvcConfigurer.class);
                });
    }
}
