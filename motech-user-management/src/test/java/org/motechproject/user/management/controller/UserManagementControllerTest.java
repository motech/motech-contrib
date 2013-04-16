package org.motechproject.user.management.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.user.management.domain.UserRoles;
import org.motechproject.user.management.service.UserManagementService;
import org.springframework.http.MediaType;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class UserManagementControllerTest {

    UserManagementController userManagementController;

    @Mock
    UserManagementService userManagementService;
    @Mock
    UserRoles userRoles;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(userRoles.all()).thenReturn(asList("PROVIDER", "CMF_ADMIN"));
        userManagementController = new UserManagementController(userManagementService, userRoles);
    }

    @Test
    public void shouldDisplayFilteredUserSpecificPage() throws Exception {
        standaloneSetup(userManagementController)
                .build()
                .perform(get("/userManagement/list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", userRoles.all()))
                .andExpect(forwardedUrl("userManagement/list"));
    }

    @Test
    public void shouldResetPasswordForTheGivenUser() throws Exception {
        String newPassword = "newPassword";
        String userName = "userName";

        when(userManagementService.resetPassword(userName, newPassword)).thenReturn(true);

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/resetPassword").param("userName", userName).param(newPassword, newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));

        verify(userManagementService).resetPassword(userName, newPassword);
    }

    @Test
    public void shouldReturnFailureMessageWhenChangingPasswordForTheGivenUser() throws Exception {
        String newPassword = "newPassword";
        String userName = "userName";

        when(userManagementService.resetPassword(userName, newPassword)).thenReturn(false);

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/resetPassword").param("userName", userName).param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("Could not update password"));

        verify(userManagementService).resetPassword(userName, newPassword);
    }

    @Test
    public void shouldActivateTheGivenUserWithNewPassword() throws Exception {
        String newPassword = "newPassword";
        String userName = "userName";

        when(userManagementService.activateUser(userName)).thenReturn(true);
        when(userManagementService.resetPassword(userName, newPassword)).thenReturn(true);

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/activateUser").param("userName", userName).param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(content().string("User has been activated"));

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
                .andExpect(content().string("User could not be activated"));
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
    public void shouldDeactivateTheGivenUser() throws Exception {
        String userName = "userName";

        when(userManagementService.deactivate(userName)).thenReturn(true);

        standaloneSetup(userManagementController)
                .build()
                .perform(get("/userManagement/deactivateUser").param("userName", userName))
                .andExpect(status().isOk())
                .andExpect(content().string("User has been de-activated"));

        verify(userManagementService).deactivate(userName);
    }

    @Test
    public void shouldReturnErrorMessageIfDeactivatingTheGivenUserFailed() throws Exception {
        String userName = "userName";
        when(userManagementService.deactivate(userName)).thenReturn(false);

        standaloneSetup(userManagementController)
                .build()
                .perform(get("/userManagement/deactivateUser").param("userName", userName))
                .andExpect(status().isOk())
                .andExpect(content().string("User could not be de-activated"));

        verify(userManagementService).deactivate(userName);
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

    @Test
    public void shouldAddANewUser() throws Exception {

        MotechWebUser expectedUser = new MotechWebUser("username", "password", "externalId", asList("PROVIDER", "CMF_ADMIN"));

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/addNewUser").contentType(MediaType.APPLICATION_JSON)
                        .body(getJSON(expectedUser).getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().string("User has been created"));

        ArgumentCaptor<MotechWebUser> motechWebUserArgumentCaptor = ArgumentCaptor.forClass(MotechWebUser.class);
        verify(userManagementService).addNewUser(motechWebUserArgumentCaptor.capture());

        MotechWebUser user = motechWebUserArgumentCaptor.getValue();
        assertEquals(expectedUser, user);
    }

    @Test
    public void shouldThrowExceptionIfUnableTOAddANewUser() throws Exception {
        MotechWebUser expectedUser = new MotechWebUser("username", "password", "externalId", asList("PROVIDER", "CMF_ADMIN"));

        when(userManagementService.addNewUser(expectedUser)).thenThrow(new WebSecurityException("error"));

        standaloneSetup(userManagementController)
                .build()
                .perform(post("/userManagement/addNewUser").contentType(MediaType.APPLICATION_JSON)
                        .body(getJSON(expectedUser).getBytes()))
                .andExpect(status().isInternalServerError());

        ArgumentCaptor<MotechWebUser> motechWebUserArgumentCaptor = ArgumentCaptor.forClass(MotechWebUser.class);
        verify(userManagementService).addNewUser(motechWebUserArgumentCaptor.capture());

        MotechWebUser user = motechWebUserArgumentCaptor.getValue();
        assertEquals(expectedUser, user);
    }

    protected String getJSON(Object object) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writer().writeValueAsString(object);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
