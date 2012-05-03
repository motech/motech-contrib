package org.motechproject.security.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RolesTest {

    @Test
    public void shouldFindRoleByName() {
        Roles roles = new Roles();
        roles.add(new Role("create"));
        roles.add(new Role("edit"));

        assertTrue(roles.hasRole("create"));
        assertTrue(roles.hasRole("edit"));
        assertFalse(roles.hasRole("delete"));
    }
}
