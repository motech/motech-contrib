package org.motechproject.user.management.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.service.MotechUser;
import org.motechproject.user.management.service.UserManagementService;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserManagementControllerTest {

    UserManagementController userManagementController;

    @Mock
    UserManagementService userManagementService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        userManagementController = new UserManagementController(userManagementService);
    }

    @Test
    public void shouldDisplayFilteredUserSpecificPage() throws Exception {
        String admin = "admin";
        String provider = "provider";

        List<MotechUser> adminUsers = asList(mock(MotechUser.class));
        List<MotechUser> providerUsers = asList(mock(MotechUser.class));
        List users = new ArrayList(adminUsers);
        users.addAll(providerUsers);

        when(userManagementService.findByRole(admin)).thenReturn(adminUsers);
        when(userManagementService.findByRole(provider)).thenReturn(providerUsers);

        standaloneSetup(userManagementController)
                .build()
                .perform(get("/userManagement/list").param("userRoles", admin).param("userRoles", provider))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", users))
                .andExpect(forwardedUrl("userManagement/list"));

        verify(userManagementService).findByRole(admin);
        verify(userManagementService).findByRole(provider);
    }

    @Test
    public void shouldChangePasswordForTheGivenUser() throws Exception {
        String newPassword = "newPassword";
        String oldPassword = "oldPassword";
        String userName = "userName";

        MotechUser motechUser = mock(MotechUser.class);
        when(userManagementService.changePassword(userName, oldPassword, newPassword)).thenReturn(motechUser);

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/changePassword").param("userName", userName).param("currentPassword", oldPassword).param(newPassword, newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(userManagementService).changePassword(userName, oldPassword, newPassword);
    }

    @Test
    public void shouldReturnFailureMessageWhenChangingPasswordForTheGivenUser() throws Exception {
        String newPassword = "newPassword";
        String oldPassword = "oldPassword";
        String userName = "userName";

        when(userManagementService.changePassword(userName, oldPassword, newPassword)).thenReturn(null);

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/changePassword").param("userName", userName).param("currentPassword", oldPassword).param(newPassword, newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("Current Password you entered is incorrect"));

        verify(userManagementService).changePassword(userName, oldPassword, newPassword);
    }

    @Test
    public void shouldActivatePasswordForTheGivenUser() throws Exception {
        String newPassword = "newPassword";
        String userName = "userName";

        when(userManagementService.activateUser(userName)).thenReturn(true);
        when(userManagementService.resetPassword(userName, newPassword)).thenReturn(true);

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/activateUser").param("userName", userName).param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(userManagementService).activateUser(userName);
        verify(userManagementService).resetPassword(userName, newPassword);
    }

    @Test
    public void shouldReturnErrorMessageIfResetPasswordFailed() throws Exception {
        String newPassword = "newPassword";
        String userName = "userName";

        when(userManagementService.resetPassword(userName, newPassword)).thenReturn(false);

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/activateUser").param("userName", userName).param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("User does not exist"));
    }

    @Test
    public void shouldReturnErrorMessageIfActivateUserFailed() throws Exception {
        String newPassword = "newPassword";
        String userName = "userName";

        when(userManagementService.resetPassword(userName, newPassword)).thenReturn(true);
        when(userManagementService.activateUser(userName)).thenReturn(false);

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/activateUser").param("userName", userName).param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));
    }

    @Test
    public void shouldRemoveUser() throws Exception {
        String userName = "userName";

        when(userManagementService.removeUser(userName)).thenReturn(true);

        standaloneSetup(userManagementController)
                .build()
                .perform(get("/userManagement/removeUser").param("userName", userName))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(userManagementService).removeUser(userName);
    }

    @Test
    public void shouldThrowErrorIfRemoveUserFails() throws Exception {
        String userName = "userName";

        when(userManagementService.removeUser(userName)).thenReturn(false);

        standaloneSetup(userManagementController)
                .build()
                .perform(get("/userManagement/removeUser").param("userName", userName))
                .andExpect(status().isOk())
                .andExpect(content().string("error"));

        verify(userManagementService).removeUser(userName);
    }
}
