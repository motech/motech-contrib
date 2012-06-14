package org.motechproject.security.service;

import org.ektorp.CouchDbConnector;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.security.authentication.MotechPasswordEncoder;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationWebSecurityContext.xml")
public class MotechAuthenticationServiceTest extends SpringIntegrationTest {

    @Autowired
    MotechAuthenticationService motechAuthenticationService;

    @Autowired
    AllMotechWebUsers allMotechWebUsers;

    @Autowired
    @Qualifier("webSecurityDbConnector")
    CouchDbConnector connector;

    @Autowired
    private MotechPasswordEncoder passwordEncoder;

    @Test
    public void testRegister() throws WebSecurityException {
        motechAuthenticationService.register("userName", "password", "1234", asList("IT_ADMIN", "DB_ADMIN"));
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertNotNull(motechWebUser);
        assertEquals("IT_ADMIN", motechWebUser.getRoles().get(0));
        assertEquals("DB_ADMIN", motechWebUser.getRoles().get(1));
    }

    @Test
    public void shouldActivateUser() throws WebSecurityException {
        motechAuthenticationService.register("userName", "password", "1234", asList("IT_ADMIN", "DB_ADMIN"), false);
        motechAuthenticationService.activateUser("userName");
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertTrue(motechWebUser.isActive());
    }

    @Test(expected = WebSecurityException.class)
    public void shouldThrowExceptionIfUserNameIsEmptyForRegister() throws WebSecurityException {
        motechAuthenticationService.register("", "password", "ext_id", new ArrayList<String>());
    }

    @Test(expected = WebSecurityException.class)
    public void shouldThrowExceptionIfUserNameIsEmptyForRegisterWithActiveInfo() throws WebSecurityException {
        motechAuthenticationService.register("", "password", "ext_id", new ArrayList<String>(), true);

    }

    @Test(expected = WebSecurityException.class)
    public void shouldThrowExceptionIfPasswordIsEmptyForRegister() throws WebSecurityException {
        motechAuthenticationService.register("user", "", "ext_id", new ArrayList<String>());
    }

    @Test(expected = WebSecurityException.class)
    public void shouldThrowExceptionIfUserNameisNull() throws WebSecurityException {
        motechAuthenticationService.register(null, "", "ext_id", new ArrayList<String>());
    }

    @Test
    public void shouldNotActivateInvalidUser() throws WebSecurityException {
        motechAuthenticationService.register("userName", "password", "1234", asList("IT_ADMIN", "DB_ADMIN"), false);
        motechAuthenticationService.activateUser("userName1");
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertFalse(motechWebUser.isActive());
    }

    @Test
    public void shouldCreateActiveUserByDefault() throws WebSecurityException {
        motechAuthenticationService.register("userName", "password", "1234", asList("IT_ADMIN", "DB_ADMIN"));
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertTrue(motechWebUser.isActive());
    }

    @Test
    public void shouldCreateInActiveUser() throws WebSecurityException {
        motechAuthenticationService.register("userName", "password", "1234", asList("IT_ADMIN", "DB_ADMIN"), false);
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertFalse(motechWebUser.isActive());
    }

    @Test
    public void testPasswordEncoding() throws WebSecurityException {
        String plainTextPassword = "testpassword";
        motechAuthenticationService.register("testuser", plainTextPassword, "entity1", asList("ADMIN"));
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("testuser");
        assertTrue(passwordEncoder.isPasswordValid(motechWebUser.getPassword(), plainTextPassword));
    }

    @Test
    public void shouldChangePassword() throws WebSecurityException {
        motechAuthenticationService.register("userName", "password", "1234", asList("IT_ADMIN", "DB_ADMIN"));
        motechAuthenticationService.changePassword("userName", "password", "newPassword");
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");
        assertTrue(passwordEncoder.isPasswordValid(motechWebUser.getPassword(), "newPassword"));
    }

    @Test
    public void shouldNotChangePasswordWithoutOldPassword() throws WebSecurityException {
        motechAuthenticationService.register("userName", "password", "1234", asList("IT_ADMIN", "DB_ADMIN"));
        motechAuthenticationService.changePassword("userName", "foo", "newPassword");
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");
        assertTrue(passwordEncoder.isPasswordValid(motechWebUser.getPassword(), "password"));
    }

    @Test
    public void hasUserShouldReturnTrueOnlyIfUserExists() throws WebSecurityException {
        assertFalse(motechAuthenticationService.hasUser("username"));
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"));
        assertTrue(motechAuthenticationService.hasUser("username"));
    }

    @Test
    public void shouldValidateUserCredentials() throws WebSecurityException {
        motechAuthenticationService.register("username", "password", "1234", asList("IT_ADMIN"));
        assertNotNull(motechAuthenticationService.retrieveUserByCredentials("username", "password"));
        assertNull(motechAuthenticationService.retrieveUserByCredentials("username", "passw550rd"));
    }

    @After
    public void tearDown() {
        markForDeletion(allMotechWebUsers.getAll());
        super.tearDown();
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return connector;
    }
}
