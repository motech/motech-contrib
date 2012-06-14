package org.motechproject.security.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.security.authentication.MotechPasswordEncoder;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationWebSecurityContext.xml")
public class AllMotechWebUsersIT {

    @Autowired
    AllMotechWebUsers allMotechWebUsers;
    @Autowired
    MotechPasswordEncoder passwordEncoder;

    @Test
    public void findByUserName() {
        MotechWebUser motechWebUser = new MotechWebUser("testuser", "testpassword", "id", asList("ADMIN"));
        allMotechWebUsers.add(motechWebUser);

        MotechWebUser testWebUser = allMotechWebUsers.findByUserName("testuser");
        assertEquals("testuser", testWebUser.getUserName());
    }

    @Test
    public void findByUserNameShouldBeCaseInsensitive() {
        String userName = "TestUser";
        allMotechWebUsers.add(new MotechWebUser(userName, "testpassword", "id", asList("ADMIN")));

        assertNotNull(allMotechWebUsers.findByUserName("TESTUSER"));
    }

    @Test
    public void shouldNotCreateNewAccountIfUserAlreadyExists() throws WebSecurityException {
        String userName = "username";

        allMotechWebUsers.add(new MotechWebUser(userName, "testpassword", "id", asList("ADMIN")));
        allMotechWebUsers.add(new MotechWebUser(userName, "testpassword1", "id2", asList("ADMIN")));


        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName("userName");
        assertEquals(1, allMotechWebUsers.getAll().size());
        assertEquals("testpassword", motechWebUser.getPassword());
        assertEquals("id", motechWebUser.getExternalId());

    }


    @Test
    public void shouldListWebUsersByRole() {
        MotechWebUser provider1 = new MotechWebUser("provider1", "testpassword", "id1", asList("PROVIDER"));
        MotechWebUser provider2 = new MotechWebUser("provider2", "testpassword", "id2", asList("PROVIDER"));
        MotechWebUser cmfAdmin = new MotechWebUser("cmfadmin", "testpassword", "id3", asList("CMFADMIN"));
        MotechWebUser itAdmin = new MotechWebUser("itadmin", "testpassword", "id4", asList("ITADMIN"));
        allMotechWebUsers.add(provider1);
        allMotechWebUsers.add(provider2);
        allMotechWebUsers.add(cmfAdmin);
        allMotechWebUsers.add(itAdmin);

        List<MotechWebUser> providers = allMotechWebUsers.findByRole("PROVIDER");
        assertEquals(asList("id1", "id2"), extract(providers, on(MotechWebUser.class).getExternalId()));
    }

    @Test
    public void findByUseridShouldReturnNullIfuserNameIsNull() {
        assertNull(null, allMotechWebUsers.findByUserName(null));
    }

    @After
    public void tearDown() {
        allMotechWebUsers.removeAll();
    }
}
