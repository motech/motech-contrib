package org.motechproject.security.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.security.domain.MotechWebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationWebSecurityContext.xml")
public class MotechAuthenticationServiceTest extends TestCase {

    @Autowired
    MotechAuthenticationService motechAuthenticationService;

    @Test
    public void testRegister() throws Exception {
        MotechWebUser user = createUser();
       // motechAuthenticationService.register(user);
        //MotechWebUser motechWebUser = motechAuthenticationService.findByUserName(user.getUserName());
        //assertNotNull(motechWebUser);

        //motechAuthenticationService.remove(motechWebUser);


    }

    private MotechWebUser createUser() {
        MotechWebUser webUser = new MotechWebUser("1234","guest","guest");

        return webUser;
    }
}
