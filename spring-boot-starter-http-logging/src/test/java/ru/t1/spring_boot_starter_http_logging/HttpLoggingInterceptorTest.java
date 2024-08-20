package ru.t1.spring_boot_starter_http_logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import ru.t1.spring_boot_starter_http_logging.logger.HttpLoggingInterceptor;
import ru.t1.spring_boot_starter_http_logging.properties.HttpLoggingProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

public class HttpLoggingInterceptorTest {

    private HttpLoggingProperties properties;
    private HttpLoggingInterceptor interceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Logger logger;

    @BeforeEach
    void setUp() throws Exception {
        properties = new HttpLoggingProperties();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        logger = Mockito.spy(LoggerFactory.getLogger(HttpLoggingInterceptor.class));
        interceptor = new HttpLoggingInterceptor(properties);

        Field loggerField = HttpLoggingInterceptor.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        loggerField.set(interceptor, logger);
    }

    @Test
    void testLoggingEnabled() throws Exception {
        properties.setEnabled(true);
        properties.setLevel(Level.INFO);

        boolean result = interceptor.preHandle(request, response, null);
        assertThat(result).isTrue();
        assertThat(request.getAttribute("startTime")).isNotNull();

        interceptor.afterCompletion(request, response, null, null);
        verify(logger, times(2)).info(anyString(), any(Object[].class));
    }

    @Test
    void testLoggingDisabled() throws Exception {
        properties.setEnabled(false);

        boolean result = interceptor.preHandle(request, response, null);
        assertThat(result).isTrue();
        assertThat(request.getAttribute("startTime")).isNull();

        interceptor.afterCompletion(request, response, null, null);
        verify(logger, never()).info(anyString(), any(Object[].class));
    }

    @Test
    void testLoggingException() throws Exception {
        properties.setEnabled(true);
        Exception ex = new RuntimeException("Test Exception");

        request.setAttribute("startTime", System.currentTimeMillis());
        interceptor.afterCompletion(request, response, null, ex);
        verify(logger).error("Request resulted in exception: {}, message: {}", ex, ex.getMessage());
    }

    @Test
    void testLoggingAtDifferentLevels() throws Exception {
        properties.setEnabled(true);

        // Уровень DEBUG
        properties.setLevel(Level.DEBUG);
        interceptor.preHandle(request, response, null);
        interceptor.afterCompletion(request, response, null, null);
        verify(logger, atLeastOnce()).debug(anyString(), any(Object[].class));

        // Уровень INFO
        reset(logger);
        properties.setLevel(Level.INFO);
        interceptor.preHandle(request, response, null);
        interceptor.afterCompletion(request, response, null, null);
        verify(logger, atLeastOnce()).info(anyString(), any(Object[].class));

        // Уровень WARN
        reset(logger);
        properties.setLevel(Level.WARN);
        interceptor.preHandle(request, response, null);
        interceptor.afterCompletion(request, response, null, null);
        verify(logger, atLeastOnce()).warn(anyString(), any(Object[].class));

        // Уровень ERROR
        reset(logger);
        properties.setLevel(Level.ERROR);
        interceptor.preHandle(request, response, null);
        interceptor.afterCompletion(request, response, null, null);
        verify(logger, atLeastOnce()).error(anyString(), any(Object[].class));
    }

    @Test
    void testLoggingInterceptor() throws Exception {
        HttpLoggingProperties properties = new HttpLoggingProperties();
        properties.setEnabled(true);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(properties);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        interceptor.preHandle(request, response, new Object());
        interceptor.afterCompletion(request, response, new Object(), null);
    }
}



