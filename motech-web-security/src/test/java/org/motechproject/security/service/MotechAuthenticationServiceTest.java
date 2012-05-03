package org.motechproject.security.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationWebSecurityContext.xml")
public class MotechAuthenticationServiceTest extends TestCase {

    @Autowired
    MotechAuthenticationService motechAuthenticationService;

    @Autowired
    AllMotechWebUsers allMotechWebUsers;

    @Test
    public void testRegister() throws Exception {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"));
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");

        assertNotNull(motechWebUser);
        assertEquals("IT_ADMIN", motechWebUser.getRoles().get(0).getName());
        assertEquals("DB_ADMIN", motechWebUser.getRoles().get(1).getName());
        allMotechWebUsers.remove(motechWebUser);
    }
    
    @Test
    public void testAuthenticate() throws Exception {
        motechAuthenticationService.register("userName", "password", "1234", Arrays.asList("IT_ADMIN", "DB_ADMIN"));

        AuthenticatedUser authenticatedUser = motechAuthenticationService.authenticate("userName", "password");

        assertNotNull(authenticatedUser);
        assertEquals("userName", authenticatedUser.getUsername());
        assertEquals("1234", authenticatedUser.getExternalId());

        motechAuthenticationService.remove("userName");
        assertNull(allMotechWebUsers.findByUserName("userName"));
    }
}
