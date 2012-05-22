package org.motechproject.security.service;

import org.ektorp.CouchDbConnector;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;


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

    @Test
    public void testRegister() {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"));
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertNotNull(motechWebUser);
        assertEquals("IT_ADMIN", motechWebUser.getRoles().get(0).getName());
        assertEquals("DB_ADMIN", motechWebUser.getRoles().get(1).getName());
    }

    @Test
    public void shouldActivateUser() {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"), false);
        motechAuthenticationService.activateUser("userName");
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertTrue(motechWebUser.isActive());
    }

    @Test
    public void shouldNotActivateInvalidUser() {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"), false);
        motechAuthenticationService.activateUser("userName1");
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertFalse(motechWebUser.isActive());
    }

    @Test
    public void shouldCreateActiveUserByDefault() {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"));
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertTrue(motechWebUser.isActive());
    }

    @Test
    public void shouldCreateInActiveUser() {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"), false);
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertFalse(motechWebUser.isActive());
    }

    @Test
    public void testAuthenticate() {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"));

        AuthenticatedUser authenticatedUser = motechAuthenticationService.authenticate("userName", "password");

        assertNotNull(authenticatedUser);
        assertEquals("userName", authenticatedUser.getUsername());
        assertEquals("1234", authenticatedUser.getExternalId());

        motechAuthenticationService.remove("userName");
        assertNull(allMotechWebUsers.findByUserName("userName"));
    }

    @Test
    public void testChangePassword() {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"));

        AuthenticatedUser user = motechAuthenticationService.changePassword("userName", "newPassword");
        assertEquals("newPassword", user.getPassword());
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
