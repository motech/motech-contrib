package org.motechproject.user.management.domain;

import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;

import static org.junit.Assert.*;

public class UserManagementFilterTest {

    @Test
    public void shouldReturnUserNameAndRoleFromFilterParams(){
        FilterParams  filterParams = new FilterParams();
        filterParams.put("userName", "user name");
        filterParams.put("role", "User");

        UserManagementFilter filter = new UserManagementFilter(filterParams);

        assertTrue(filter.hasUserName());
        assertTrue(filter.hasRole());
        assertEquals("user name", filter.getUserName());
        assertEquals("User", filter.getRole());
    }

    @Test
    public void shouldRemoveEmptyFilterParams(){
        FilterParams  filterParams = new FilterParams();
        filterParams.put("userName", " ");
        filterParams.put("role", "  ");

        UserManagementFilter filter = new UserManagementFilter(filterParams);

        assertNull(filter.getUserName());
        assertNull(filter.getRole());
        assertFalse(filter.hasUserName());
        assertFalse(filter.hasRole());
    }

}
