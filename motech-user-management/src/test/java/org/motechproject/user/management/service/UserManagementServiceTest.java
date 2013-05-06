package org.motechproject.user.management.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.user.management.domain.UserRoles;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserManagementServiceTest {

    @Mock
    MotechAuthenticationService motechAuthenticationService;
    @Mock
    AllMotechWebUsers allMotechWebUsers;

    UserManagementService userManagementService;
    @Mock
    private UserRoles userRoles;

    @Before
    public void setUp() {
        initMocks(this);
        userManagementService = new UserManagementService(motechAuthenticationService, allMotechWebUsers, userRoles);
    }

    @Test
    public void shouldFindUsersBelongsToARole() {
        String roleName = "roleName";
        List<MotechUser> users = new ArrayList<>();
        when(motechAuthenticationService.findByRole(roleName)).thenReturn(users);

        List<MotechUser> expectedUsers = userManagementService.findByRole(roleName);

        assertThat(expectedUsers, is(users));
    }

    @Test
    public void shouldActivateGivenUser() {
        String userName = "userName";
        when(motechAuthenticationService.activateUser(userName)).thenReturn(true);

        Boolean activatedResult = userManagementService.activateUser(userName);
        assertTrue(activatedResult);
        verify(motechAuthenticationService).activateUser(userName);
    }

    @Test
    public void shouldDeleteGivenUser() {
        String userName = "userName";
        when(motechAuthenticationService.remove(userName)).thenReturn(true);

        Boolean activatedResult = userManagementService.removeUser(userName);
        assertTrue(activatedResult);
        verify(motechAuthenticationService).remove(userName);
    }

    @Test
    public void shouldResetPasswordForGivenUser(){
        String userName = "userName";
        String newPassword = "newPassword";
        when(motechAuthenticationService.resetPassword(userName, newPassword)).thenReturn(true);

        Boolean resetPasswordResult = userManagementService.resetPassword(userName, newPassword);

        assertTrue(resetPasswordResult);
        verify(motechAuthenticationService).resetPassword(userName, newPassword);
    }

    @Test
    public void shouldDeactivateGivenUser(){
        String username = "username";
        when(motechAuthenticationService.deactivateUser(username)).thenReturn(true);

        assertTrue(userManagementService.deactivate(username));

        verify(motechAuthenticationService).deactivateUser(username);
    }

    @Test
    public void shouldFilterOnUserName(){
        FilterParams filterParams = new FilterParams();
        String userName = "abc";
        filterParams.put("userName", userName);

        MotechWebUser expectedUser = mock(MotechWebUser.class);
        when(allMotechWebUsers.findByUserName(userName)).thenReturn(expectedUser);

        PageResults pageResults = userManagementService.page(1, 20, filterParams, new SortParams());

        assertEquals(1, pageResults.getTotalRows().intValue());
        assertEquals(1, pageResults.getResults().size());
        assertEquals(expectedUser, pageResults.getResults().get(0));
    }

    @Test
    public void shouldDisplayOnlyAllowedUserRolesAfterFilteringByUserName(){
        FilterParams filterParams = new FilterParams();
        String userName = "abc";
        filterParams.put("userName", userName);

        when(userRoles.all()).thenReturn(asList("ROLE1", "ROLE2"));

        MotechWebUser expectedUser = mock(MotechWebUser.class);
        when(expectedUser.getRoles()).thenReturn(asList("ROLE3"));

        when(allMotechWebUsers.findByUserName(userName)).thenReturn(expectedUser);

        PageResults pageResults = userManagementService.page(1, 20, filterParams, new SortParams());

        assertEquals(0, pageResults.getTotalRows().intValue());
        assertEquals(0, pageResults.getResults().size());
    }

    @Test
    public void shouldFilterOnUserName_whenThereAreNoResults(){
        FilterParams filterParams = new FilterParams();
        String userName = "abc";
        filterParams.put("userName", userName);

        when(allMotechWebUsers.findByUserName(userName)).thenReturn(null);

        PageResults pageResults = userManagementService.page(1, 20, filterParams, new SortParams());

        assertEquals(0, pageResults.getTotalRows().intValue());
        assertEquals(0, pageResults.getResults().size());
    }

    @Test
    public void shouldFilterOnRole(){
        FilterParams filterParams = new FilterParams();
        String roleName = "USER";
        filterParams.put("role", roleName);

        MotechWebUser motechWebUser = mock(MotechWebUser.class);

        List<MotechWebUser> expectedUsers = asList(motechWebUser);

        when(allMotechWebUsers.findByRole(roleName, 0, 20)).thenReturn(expectedUsers);
        when(allMotechWebUsers.countByRole(roleName)).thenReturn(2);

        PageResults pageResults = userManagementService.page(1, 20, filterParams, new SortParams());

        assertEquals(2, pageResults.getTotalRows().intValue());
        assertEquals(expectedUsers, pageResults.getResults());
    }

    @Test
    public void shouldFilterOnRoleWithNoResults(){
        FilterParams filterParams = new FilterParams();
        String roleName = "USER";
        filterParams.put("role", roleName);


        List<MotechWebUser> expectedUsers = new ArrayList<>();

        when(allMotechWebUsers.findByRole(roleName, 0, 20)).thenReturn(expectedUsers);
        when(allMotechWebUsers.countByRole(roleName)).thenReturn(0);

        PageResults pageResults = userManagementService.page(1, 20, filterParams, new SortParams());

        assertEquals(0, pageResults.getTotalRows().intValue());
        assertEquals(expectedUsers, pageResults.getResults());
    }

    @Test
    public void shouldReturnNoUsersIfNoFiltersApplied(){
        FilterParams filterParams = new FilterParams();
        List<MotechWebUser> emptyUsers = new ArrayList<>();

        PageResults pageResults = userManagementService.page(1, 20, filterParams, new SortParams());

        assertEquals(0, pageResults.getTotalRows().intValue());
        assertEquals(emptyUsers, pageResults.getResults());
        verifyZeroInteractions(allMotechWebUsers);
    }

    @Test
    public void shouldAddNewUser() throws WebSecurityException {
        MotechWebUser motechWebUser = new MotechWebUser();

        userManagementService.addNewUser(motechWebUser);

        verify(motechAuthenticationService).register(motechWebUser.getUserName(), motechWebUser.getPassword(), motechWebUser.getExternalId(), motechWebUser.getRoles());
    }

}

