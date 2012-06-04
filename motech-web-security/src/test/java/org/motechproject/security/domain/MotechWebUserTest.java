package org.motechproject.security.domain;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MotechWebUserTest {

    @Test
    public void userNameShouldBeSetToLowercase() {

        MotechWebUser user = new MotechWebUser("TestUser", "p@ssw0rd", "", null);
        assertEquals("testuser", user.getUserName());
    }

    @Test
    public void shouldHandleNullValueForUserName() {
        MotechWebUser user = new MotechWebUser(null, "p@ssw0rd", "", null);
        assertEquals(null, user.getUserName());
    }

}
