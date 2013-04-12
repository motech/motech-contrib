package org.motechproject.user.management.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserManagementServiceTest {

    @Mock
    MotechAuthenticationService motechAuthenticationService;

    UserManagementService userManagementService;

    @Before
    public void setUp() {
        initMocks(this);
        userManagementService = new UserManagementService(motechAuthenticationService);
    }

    @Test
    public void shouldChangePassword() {
        String userName = "userName";
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";

        MotechUser motechUser = mock(MotechUser.class);
        when(motechAuthenticationService.changePassword(userName, currentPassword, newPassword)).thenReturn(motechUser);

        MotechUser foundUser = userManagementService.changePassword(userName, currentPassword, newPassword);
        assertEquals(foundUser, motechUser);

        verify(motechAuthenticationService, times(1)).changePassword(userName, currentPassword, newPassword);
    }

    @Test
    public void shouldFindUsersBelongsToARole(){
        String roleName = "roleName";
        List<MotechUser> users = new ArrayList<>();
        when(motechAuthenticationService.findByRole(roleName)).thenReturn(users);

        List<MotechUser> expectedUsers = userManagementService.findByRole(roleName);

        assertThat(expectedUsers, is(users));
    }

}

