package org.motechproject.flash;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FlashScopeFilterTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain chain;

    Cookie[] cookies = new Cookie[]{new Cookie("flash.out.name", "value")};
    ArgumentCaptor<Cookie> cookieCaptor = forClass(Cookie.class);


    FlashScopeFilter filter = new FlashScopeFilter();

    @Before
    public void setup() {
        initMocks(this);
        when(request.getCookies()).thenReturn(cookies);
    }

    @Test
    public void shouldPassFlashParametersAsFlashIn() throws IOException, ServletException {
        Hashtable<String, String> nonFlashAttributes = new Hashtable<String, String>();
        nonFlashAttributes.put("name", "value");

        when(request.getAttributeNames()).thenReturn(nonFlashAttributes.keys());
        filter.doFilter(request, response, chain);
        verify(request).setAttribute("flash.in.name", "value");
    }

    @Test
    public void shouldCreateCookieForFlashOuts() throws IOException, ServletException {
        Hashtable<String, String> nonFlashAttributes = new Hashtable<String, String>();
        nonFlashAttributes.put("flash.out.name", "value");

        when(request.getAttributeNames()).thenReturn(nonFlashAttributes.keys());
        when(request.getAttribute("flash.out.name")).thenReturn("value");

        filter.doFilter(request, response, chain);
        verify(response).addCookie(cookieCaptor.capture());
        assertEquals(cookieCaptor.getValue().getName(), "flash.out.name");
    }

}
