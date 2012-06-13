package org.motechproject.security.authentication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotechAuthenticationProviderTest {

    @Mock
    private AllMotechWebUsers allMotechWebUsers;
    @Mock
    private MotechPasswordEncoder passwordEncoder;

    private MotechAuthenticationProvider authenticationProvider;

    @Before
    public void setup() {
        initMocks(this);
        authenticationProvider = new MotechAuthenticationProvider(allMotechWebUsers, passwordEncoder);
    }

    @Test
    public void shouldRetrieveUserFromDatabase() {
        MotechWebUser motechWebUser = new MotechWebUser("bob", "encodedPassword", "entity_1", asList("some_role"));
        when(allMotechWebUsers.findByUserName("bob")).thenReturn(motechWebUser);

        MotechAuthenticationProvider authenticationProvider = new MotechAuthenticationProvider(allMotechWebUsers, passwordEncoder);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("bob", "password");
        UserDetails user = authenticationProvider.retrieveUser("bob", authentication);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(motechWebUser, authentication.getDetails());
    }

    @Test(expected = AuthenticationException.class)
    public void shouldThrowExceptionIfUserDoesntExist() {
        when(allMotechWebUsers.findByUserName("bob")).thenReturn(null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("bob", "password");
        authenticationProvider.retrieveUser("bob", authentication);
    }

    @Test(expected = AuthenticationException.class)
    public void shouldThrowExceptionIfUserIsInactive() {
        MotechWebUser motechWebUser = new MotechWebUser("bob", "encodedPassword", "entity_1", asList("some_role"));
        motechWebUser.setActive(false);
        when(allMotechWebUsers.findByUserName("bob")).thenReturn(motechWebUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("bob", "password");
        authenticationProvider.retrieveUser("bob", authentication);
    }

    @Test
    public void shouldAuthenticateUser() {
        when(passwordEncoder.isPasswordValid("encodedPassword", "password")).thenReturn(true);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("bob", "password");
        UserDetails user = mock(UserDetails.class);
        when(user.getPassword()).thenReturn("encodedPassword");

        authenticationProvider.additionalAuthenticationChecks(user, authentication);
    }

    @Test(expected = AuthenticationException.class)
    public void shouldNotAuthenticateEmptyPassword() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("bob", "");
        UserDetails user = mock(UserDetails.class);
        when(user.getPassword()).thenReturn("encodedPassword");

        authenticationProvider.additionalAuthenticationChecks(user, authentication);
    }

    @Test(expected = AuthenticationException.class)
    public void shouldNotAuthenticateWrongPassword() {
        when(passwordEncoder.isPasswordValid("encodedPassword", "password")).thenReturn(false);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("bob", "");
        UserDetails user = mock(UserDetails.class);
        when(user.getPassword()).thenReturn("encodedPassword");

        authenticationProvider.additionalAuthenticationChecks(user, authentication);
    }
}
