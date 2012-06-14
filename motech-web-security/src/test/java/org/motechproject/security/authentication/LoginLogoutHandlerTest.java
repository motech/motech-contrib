package org.motechproject.security.authentication;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechUser;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginLogoutHandlerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Authentication authentication;
    private MotechUser user;

    @Before
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        authentication = mock(Authentication.class);
        user = new MotechUser(new MotechWebUser());
        when(authentication.getDetails()).thenReturn(user);
    }

    @Test
    public void shouldAddLoggedInUserToSession() throws IOException, ServletException {
        new LoginSuccessHandler().onAuthenticationSuccess(request, response, authentication);
        assertEquals(user, request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER));
        assertNull(request.getSession().getAttribute(LoginFailureHandler.LOGIN_FAILURE));
    }

    @Test
    public void shouldOnLoginfailure() throws IOException, ServletException {
        AuthenticationException authenticationException = mock(AuthenticationException.class);
        when(authenticationException.getMessage()).thenReturn("failure");
        new LoginFailureHandler("/").onAuthenticationFailure(request, response, authenticationException);
        assertEquals("failure", request.getSession().getAttribute(LoginFailureHandler.LOGIN_FAILURE));

    }

    @Test
    public void shouldRemoveLoggedInUserFromSession() throws IOException, ServletException {
        new LogoutSuccessHandler().onLogoutSuccess(request, response, authentication);
        assertNull(request.getSession().getAttribute(LoginSuccessHandler.LOGGED_IN_USER));
    }
}
