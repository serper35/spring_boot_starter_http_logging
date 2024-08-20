package ru.t1.spring_boot_starter_http_logging.logger;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.event.Level;
import ru.t1.spring_boot_starter_http_logging.properties.HttpLoggingProperties;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class HttpLoggingInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(HttpLoggingInterceptor.class);

    private HttpLoggingProperties properties;

    public HttpLoggingInterceptor(HttpLoggingProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!properties.isEnabled()) {
            return true;
        }

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        logRequest(request);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!properties.isEnabled()) {
            return;
        }

        if (ex != null) {
            logger.error("Request resulted in exception: {}, message: {}", ex, ex.getMessage());
        }

        int responseStatus = response.getStatus();
        long duration = System.currentTimeMillis() - (long) request.getAttribute("startTime");
        logResponse(request, response, responseStatus, duration);
    }

    private void logRequest(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String sessionId = request.getSession(false) != null ? request.getSession().getId() : "N/A";
        String queryParams = request.getQueryString() != null ? request.getQueryString() : "N/A";
        String referer = request.getHeader("Referer") != null ? request.getHeader("Referer") : "N/A";
        Level logLevel = properties.getLevel();

        switch (logLevel) {
            case DEBUG ->
                    logger.debug("Incoming HTTP request: method={}, url={}, client-ip={}, user-agent={}, session-id={}, query-params={}, referer={}, headers={}",
                            method, url, clientIp, userAgent, sessionId, queryParams, referer, headers);
            case INFO ->
                    logger.info("Incoming HTTP request: method={}, url={}, client-ip={}, user-agent={}, session-id={}",
                            method, url, clientIp, userAgent, sessionId);
            case WARN -> logger.warn("Incoming HTTP request: method={}, url={}, client-ip={}",
                    method, url, clientIp);
            case ERROR -> logger.error("Incoming HTTP request: method={}, url={}, client-ip={}",
                    method, url, clientIp);
            default -> logger.info("Incoming HTTP request: method={}, url={}, client-ip={}", method, url, clientIp);
        }
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response, int responseStatus, long duration) {
        Map<String, String> headers = new HashMap<>();

        for (String headerName : response.getHeaderNames()) {
            headers.put(headerName, response.getHeader(headerName));
        }

        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String sessionId = request.getSession(false) != null ? request.getSession().getId() : "N/A";
        String queryParams = request.getQueryString() != null ? request.getQueryString() : "N/A";
        String referer = request.getHeader("Referer") != null ? request.getHeader("Referer") : "N/A";
        Level logLevel = properties.getLevel();

        switch (logLevel) {
            case DEBUG ->
                    logger.debug("Completed HTTP request: method={}, url={}, response-status={}, duration={}ms, client-ip={}, user-agent={}, session-id={}, query-params={}, referer={}, " +
                                    "headers={}",
                            method, url, responseStatus, duration, clientIp, userAgent, sessionId, queryParams, referer, headers);
            case INFO ->
                    logger.info("Completed HTTP request: method={}, url={}, response-status={}, duration={}ms, client-ip={}, user-agent={}, session-id={}",
                            method, url, responseStatus, duration, clientIp, userAgent, sessionId);
            case WARN ->
                    logger.warn("Completed HTTP request: method={}, url={}, response-status={}, duration={}ms, client-ip={}",
                            method, url, responseStatus, duration, clientIp);
            case ERROR ->
                    logger.error("Completed HTTP request: method={}, url={}, response-status={}, duration={}ms, client-ip={}",
                            method, url, responseStatus, duration, clientIp);
            default ->
                    logger.info("Completed HTTP request: method={}, url={}, response-status={}, duration={}ms, client-ip={}",
                            method, url, responseStatus, duration, clientIp);
        }
    }
}
