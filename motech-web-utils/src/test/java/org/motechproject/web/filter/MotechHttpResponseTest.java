package org.motechproject.web.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.DelegatingServletOutputStream;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MotechHttpResponseTest {

    @Mock
    private HttpServletResponse response;
    @Mock
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCaptureTheResponseBody() throws IOException {
        when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(outputStream));

        MotechHttpResponse motechHttpResponse = new MotechHttpResponse(response);
        motechHttpResponse.getOutputStream().write('c');

        String responseBody = motechHttpResponse.responseBody();
        assertEquals("c", responseBody);
    }
}
