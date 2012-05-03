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

    @After
    public void tearDown() {
        allMotechWebUsers.removeAll();
    }
}
