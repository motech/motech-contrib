package org.motechproject.security.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:applicationWebSecurityContext.xml")
public class AllMotechWebUsersIT {

    @Autowired
    AllMotechWebUsers allMotechWebUsers;

    @Test
    public void testFindByUserName() {
        MotechWebUser motechWebUser = new MotechWebUser("testuser", "testpassword", "Administrator", "id", Arrays.asList(new Role("ADMIN")));
        allMotechWebUsers.add(motechWebUser);

        MotechWebUser testUser = allMotechWebUsers.findByUserName("testuser");
        assertNotNull(testUser);
        assertEquals("testuser", testUser.getUserName());
        assertEquals("testpassword", testUser.getPassword());
        assertEquals("Administrator", testUser.getUserType());
        assertEquals("ADMIN", testUser.getRoles().get(0).getName());

        allMotechWebUsers.remove(testUser);
        assertNull(allMotechWebUsers.findByUserName("testuser"));
    }
}
