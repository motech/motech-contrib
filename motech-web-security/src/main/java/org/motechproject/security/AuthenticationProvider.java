package org.motechproject.security;

import org.apache.commons.lang.StringUtils;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.security.service.MotechAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    public static final String PLEASE_ENTER_PASSWORD = "Please enter password";
    public static final String USER_NOT_FOUND = "The username or password you entered is incorrect";
    public static final String FAILURE = "Failure";
    public static final String SUCCESS = "Success";

    private MotechAuthenticationService authenticationService;

    @Autowired
    public AuthenticationProvider(MotechAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String password = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(password)) throw new BadCredentialsException(PLEASE_ENTER_PASSWORD);
        AuthenticatedUser user = authenticationService.authenticate(username, password);
        if (user == null) {
            throw new BadCredentialsException(USER_NOT_FOUND);
        } else {
            return user;
        }
    }
}
