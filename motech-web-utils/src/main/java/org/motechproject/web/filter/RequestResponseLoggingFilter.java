package org.motechproject.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestResponseLoggingFilter extends AbstractRequestLoggingFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MotechHttpResponse motechHttpResponse = new MotechHttpResponse(response);
        try {
            super.doFilterInternal(request, motechHttpResponse, filterChain);
        } finally {
            afterRequestCompletion(motechHttpResponse, createMessage(request, DEFAULT_AFTER_MESSAGE_PREFIX, DEFAULT_AFTER_MESSAGE_SUFFIX));
        }
    }

    private void afterRequestCompletion(HttpServletResponse response, String message) {
        String responseBody = "";
        MotechHttpResponse motechHttpResponse = (MotechHttpResponse) response;

        if (canResponseBeLogged(response) && isIncludePayload()) {
            responseBody = String.format(" Response :%s", motechHttpResponse.responseBody());
        }
        LOGGER.info(message + " " + responseBody);
    }

    private boolean canResponseBeLogged(HttpServletResponse response) {
        return response.getContentType().contains("application/json")
                || response.getContentType().contains("plain/text")
                || response.getContentType().contains("application/xml");
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        LOGGER.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        //no op using afterRequestCompletion instead
    }
}

