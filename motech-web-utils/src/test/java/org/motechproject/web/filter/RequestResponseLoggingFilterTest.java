package org.motechproject.web.filter;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = Logger.getLogger(RequestResponseLoggingFilter.class);
        logStream = new ByteArrayOutputStream();
        appender = new WriterAppender(new SimpleLayout(), logStream);
        logger.addAppender(appender);

        requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        requestResponseLoggingFilter.setIncludePayload(true);
    }

    @Test
    public void shouldLogResponseOnlyIfContentTypeIsJson() throws IOException, ServletException {
        when(response.getContentType()).thenReturn("application/json");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains("Response"));
    }

    @Test
    public void shouldNotLogResponse_IfIncludePayloadIsNotSetEvenIfContentTypeIsJson() throws IOException, ServletException {
        requestResponseLoggingFilter.setIncludePayload(false);
        when(response.getContentType()).thenReturn("application/json");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains("Response"));
    }

    @Test
    public void shouldLogResponseOnlyIfContentTypeIsPlainText() throws IOException, ServletException {
        when(response.getContentType()).thenReturn("plain/text");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains("Response"));
    }

    @Test
    public void shouldLogResponseOnlyIfContentTypeIsXml() throws IOException, ServletException {
        when(response.getContentType()).thenReturn("application/xml");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertTrue(logStream.toString().contains("Response"));
    }

    @Test
    public void shouldNotLogResponseIfContentTypeIsNotTextBased() throws IOException, ServletException {
        requestResponseLoggingFilter = new RequestResponseLoggingFilter();
        when(response.getContentType()).thenReturn("multipart");

        requestResponseLoggingFilter.doFilterInternal(request, response, chain);

        assertFalse(logStream.toString().contains("Response"));
    }
}
