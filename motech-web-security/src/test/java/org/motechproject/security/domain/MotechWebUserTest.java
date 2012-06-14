package org.motechproject.security.domain;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MotechWebUserTest {

    @Test
    public void userNameShouldBeSetToLowercase() {

        MotechWebUser webUser = new MotechWebUser("TestUser", "p@ssw0rd", "", null);
        assertEquals("testuser", webUser.getUserName());
    }

    @Test
    public void shouldHandleNullValueForUserName() {
        MotechWebUser webUser = new MotechWebUser(null, "p@ssw0rd", "", null);
        assertEquals(null, webUser.getUserName());
    }

}
