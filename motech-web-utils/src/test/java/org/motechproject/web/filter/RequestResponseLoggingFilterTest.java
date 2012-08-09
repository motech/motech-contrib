package org.motechproject.web.filter;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RequestResponseLoggingFilterTest {

    private RequestResponseLoggingFilter requestResponseLoggingFilter;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;

    private ByteArrayOutputStream logStream;
    private WriterAppender appender;
    private org.apache.log4j.Logger logger;
    private String responseContent;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = Logger.getLogger(RequestResponseLoggingFilter.class);
        logStream = new ByteArrayOutputStream();
        appender = new WriterAppender(new SimpleLayout(), logStream);
        logger.addAppender(appender);
        logger.setLevel(Level.DEBUG);

        requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        requestResponseLoggingFilter.setIncludeResponsePayload(true);
        responseContent = "with response";
    }

    @Test
    public void shouldLogResponse() throws IOException, ServletException {
        when(response.getContentType()).thenReturn("application/json");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains(responseContent));
    }

    @Test
    public void shouldNotLogResponse_IfIncludePayloadIsNotSet() throws IOException, ServletException {
        requestResponseLoggingFilter.setIncludeResponsePayload(false);
        when(response.getContentType()).thenReturn("application/json");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains(responseContent));
    }

    @Test
    public void shouldLogResponseOnlyIfContentTypeIsPlainText() throws IOException, ServletException {
        when(response.getContentType()).thenReturn("text/plain");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains(responseContent));
    }

    @Test
    public void shouldLogResponseOnlyIfContentTypeIsXml() throws IOException, ServletException {
        when(response.getContentType()).thenReturn("application/xml");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains(responseContent));
    }

    @Test
    public void shouldLogResponseOnlyIfContentTypeIsJavaScript() throws IOException, ServletException {
        when(response.getContentType()).thenReturn("application/javascript");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains(responseContent));
    }

    @Test
    public void shouldNotLogResponseIfContentTypeIsNotTextBased() throws IOException, ServletException {
        when(response.getContentType()).thenReturn("multipart");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains(responseContent));
    }

    @Test
    public void shouldNotLogResponseIfContentTypeIsNull() throws IOException, ServletException {
        when(response.getContentType()).thenReturn(null);

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains(responseContent));
    }

    @Test
    public void shouldLogResponsePayloadOnlyInDebugMode() throws IOException, ServletException {
        logger.setLevel(Level.INFO);
        when(response.getContentType()).thenReturn("application/javascript");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains(responseContent));
    }

    @Test
    public void shouldLogRequestURI() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("uri");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains("uri"));
    }

    @Test
    public void shouldLogRequestURIOnlyInDebugMode() throws IOException, ServletException {
        logger.setLevel(Level.INFO);
        when(request.getRequestURI()).thenReturn("uri");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains("uri"));
    }

    @Test
    public void shouldLogRequestPayloadOnlyIfEnabled() throws IOException, ServletException {
        requestResponseLoggingFilter.setIncludeRequestPayload(true);
        when(request.getRequestURI()).thenReturn("uri");
        when(request.getContentType()).thenReturn("application/json");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains("payload"));
    }

    @Test
    public void shouldNotLogRequestPayloadIfEnabledDisabled() throws IOException, ServletException {
        requestResponseLoggingFilter.setIncludeRequestPayload(false);
        when(request.getRequestURI()).thenReturn("uri");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains("payload"));
    }

    @Test
    public void shouldLogRequestPayloadOnlyForTextBasedContentTypes() throws IOException, ServletException {
        requestResponseLoggingFilter.setIncludeRequestPayload(true);
        when(request.getRequestURI()).thenReturn("uri");
        when(request.getContentType()).thenReturn("application/json");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains("payload"));
    }

    @Test
    public void shouldNotLogRequestPayloadForNonTextBasedContentTypes() throws IOException, ServletException {
        requestResponseLoggingFilter.setIncludeRequestPayload(true);
        when(request.getRequestURI()).thenReturn("uri");
        when(request.getContentType()).thenReturn("multipart");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains("payload"));
    }
}
