package org.motechproject.security.repository;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.domain.Role;
import org.motechproject.security.domain.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:applicationWebSecurityContext.xml")
public class AllMotechWebUsersIT {

    @Autowired
    AllMotechWebUsers allMotechWebUsers;

    @Autowired
    PBEStringEncryptor pbeStringEncryptor;

    @Test
    public void findByUserName_shouldAlsoDecryptPassword() {
        MotechWebUser motechWebUser = new MotechWebUser("testuser", "testpassword", "id", new Roles(Arrays.asList(new Role("ADMIN"))));
        allMotechWebUsers.add(motechWebUser);

        MotechWebUser testUser = allMotechWebUsers.findByUserName("testuser");
        assertNotNull(testUser);
        assertEquals("testuser", testUser.getUserName());
        assertEquals("testpassword", testUser.getPassword());
        assertEquals("ADMIN", testUser.getRoles().get(0).getName());
    }

    @Test
    public void shouldEncryptPlainTextPassword_BeforeSavingTheUser() {
        String plainTextPassword = "testpassword";
        MotechWebUser motechWebUser = new MotechWebUser("testuser", plainTextPassword, "id", new Roles(Arrays.asList(new Role("ADMIN"))));
        allMotechWebUsers.add(motechWebUser);

        assertThat(pbeStringEncryptor.decrypt(motechWebUser.getPassword()), is(plainTextPassword));
    }

    @Test
    public void shouldEncryptPlainTextPassword_OnChangePassword() {
        String userName = "testuser";
        allMotechWebUsers.add(new MotechWebUser(userName, "testpassword", "id", new Roles(Arrays.asList(new Role("ADMIN")))));

        String newPassword = "newPassword";
        allMotechWebUsers.changePassword(userName, newPassword);

        MotechWebUser testUser = allMotechWebUsers.findByUserName(userName);
        assertThat(testUser.getPassword(), is(newPassword));
    }

    @Test
    public void findByUserNameShouldBeCaseInsensitive() {
        String userName = "TestUser";
        allMotechWebUsers.add(new MotechWebUser(userName, "testpassword", "id", new Roles(Arrays.asList(new Role("ADMIN")))));

        assertNotNull(allMotechWebUsers.findByUserName("TESTUSER"));
    }

    @Test
    public void UserNameShouldbeCaseInsensitiveForChangePassword() {
        String userName = "testuser";
        allMotechWebUsers.add(new MotechWebUser(userName, "testpassword", "id", new Roles(Arrays.asList(new Role("ADMIN")))));

        String newPassword = "newPassword";
        allMotechWebUsers.changePassword("TESTUSER", newPassword);

        MotechWebUser testUser = allMotechWebUsers.findByUserName(userName);
        assertThat(testUser.getPassword(), is(newPassword));
    }

    @Test
    public void shouldListWebUsersByRole() {
        Role testRole = new Role("PROVIDER");
        MotechWebUser provider1 = new MotechWebUser("provider1", "testpassword", "id1", new Roles(Arrays.asList(testRole)));
        MotechWebUser provider2 = new MotechWebUser("provider2", "testpassword", "id2", new Roles(Arrays.asList(testRole)));
        MotechWebUser cmfAdmin = new MotechWebUser("cmfadmin", "testpassword", "id3", new Roles(Arrays.asList(new Role("CMFADMIN"))));
        MotechWebUser itAdmin = new MotechWebUser("itadmin", "testpassword", "id4", new Roles(Arrays.asList(new Role("ITADMIN"))));
        allMotechWebUsers.add(provider1);
        allMotechWebUsers.add(provider2);
        allMotechWebUsers.add(cmfAdmin);
        allMotechWebUsers.add(itAdmin);

        List<MotechWebUser> providers = allMotechWebUsers.findByRoles(testRole);
        assertNotNull(providers);
        assertFalse(providers.isEmpty());
        assertTrue(providers.contains(provider1));
        assertTrue(providers.indexOf(provider2) != -1);
        assertTrue(providers.indexOf(cmfAdmin) == -1);
        assertTrue(providers.indexOf(itAdmin) == -1);
    }

    @Test
    public void findByUseridShouldReturnNullIfuserNameIsNull() {
        assertNull(null,allMotechWebUsers.findByUserName(null));
    }

    @After
    public void tearDown() {
        allMotechWebUsers.removeAll();
    }
}
