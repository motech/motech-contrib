package org.motechproject.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger("RequestResponseLogger");

    private List<String> textBasedContentTypes = Arrays.asList("application/json", "text/plain", "application/xml", "application/javascript", "application/x-www-form-urlencoded");

    private boolean includeQueryString;
    private boolean includeRequestPayload;
    private boolean includeResponsePayload;

    public boolean isIncludeQueryString() {
        return includeQueryString;
    }

    public void setIncludeQueryString(boolean includeQueryString) {
        this.includeQueryString = includeQueryString;
    }

    public boolean isIncludeRequestPayload() {
        return includeRequestPayload;
    }

    public void setIncludeRequestPayload(boolean includeRequestPayload) {
        this.includeRequestPayload = includeRequestPayload;
    }

    public boolean isIncludeResponsePayload() {
        return includeResponsePayload;
    }

    public void setIncludeResponsePayload(boolean includeResponsePayload) {
        this.includeResponsePayload = includeResponsePayload;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isIncludeResponsePayload()) {
            response = new MotechHttpResponse(response);
        }
        if (isIncludeRequestPayload() && canRequestBeLogged(request)) {
            request = new MotechHttpRequest(request);
        }

        beforeRequest(request);

        try {
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response);
        }
    }

    private boolean canRequestBeLogged(HttpServletRequest request) {
        String contentType = request.getContentType();
        return isTextBasedContentType(contentType);
    }


    private boolean canResponseBeLogged(HttpServletResponse response) {
        String contentType = response.getContentType();
        return isTextBasedContentType(contentType);
    }

    private boolean isTextBasedContentType(String contentType) {
        if (contentType == null) return false;

        for (String allowedContentType : textBasedContentTypes) {
            if (contentType.contains(allowedContentType)) {
                return true;
            }
        }

        return false;
    }

    protected void beforeRequest(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Before request [%s]", requestURI(request)));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(stringBuilder.toString());
        }
    }

    protected void afterRequest(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("After request [%s]", requestURI(request)));

        if (isIncludeRequestPayload() && canRequestBeLogged(request)) {
            MotechHttpRequest motechHttpRequest = (MotechHttpRequest) request;
            stringBuilder.append(String.format(" with request payload [%s]", motechHttpRequest.requestBody()));
        }

        if (isIncludeResponsePayload()) {
            MotechHttpResponse motechHttpResponse = (MotechHttpResponse) response;
            if (canResponseBeLogged(motechHttpResponse)) {
                stringBuilder.append(String.format(" with response [%s]", motechHttpResponse.responseBody()));
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(stringBuilder.toString());
        }
    }

    private String requestURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        if (isIncludeQueryString()) {
            requestURI += ("?" + request.getQueryString());
        }
        return requestURI;
    }
}

