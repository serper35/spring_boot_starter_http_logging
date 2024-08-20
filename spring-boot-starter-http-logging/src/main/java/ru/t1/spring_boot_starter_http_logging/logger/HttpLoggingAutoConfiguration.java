package ru.t1.spring_boot_starter_http_logging.logger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.t1.spring_boot_starter_http_logging.properties.HttpLoggingProperties;

@Configuration
@EnableConfigurationProperties(HttpLoggingProperties.class)
@ConditionalOnProperty(prefix = "http.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
@Order(1)
public class HttpLoggingAutoConfiguration {
    private final HttpLoggingProperties properties;

    public HttpLoggingAutoConfiguration(HttpLoggingProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public HandlerInterceptor interceptor() {
        return new HttpLoggingInterceptor(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WebMvcConfigurer httpLoggingConfigurer(HttpLoggingInterceptor interceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(interceptor);
            }
        };
    }
}
