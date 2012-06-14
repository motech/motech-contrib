package org.motechproject.security.authentication;

import org.motechproject.security.domain.MotechWebUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String LOGGED_IN_USER = "loggedInUser";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MotechWebUser webUser = (MotechWebUser) authentication.getDetails();
        request.getSession().setAttribute(LOGGED_IN_USER, webUser);
        request.getSession().removeAttribute(LoginFailureHandler.LOGIN_FAILURE);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
