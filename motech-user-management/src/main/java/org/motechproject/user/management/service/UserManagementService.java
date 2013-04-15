package org.motechproject.user.management.service;

import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserManagementService {
    private MotechAuthenticationService motechAuthenticationService;

    @Autowired
    public UserManagementService(MotechAuthenticationService motechAuthenticationService) {
        this.motechAuthenticationService = motechAuthenticationService;
    }

    public MotechUser changePassword(String userName, String currentPassword, String newPassword) {
        return motechAuthenticationService.changePassword(userName, currentPassword, newPassword);
    }

    public List<MotechUser> findByRole(String role) {
        return motechAuthenticationService.findByRole(role);
    }

    public boolean activateUser(String userName) {
         return motechAuthenticationService.activateUser(userName);
    }

    public boolean removeUser(String userName) {
        return motechAuthenticationService.remove(userName);
    }

    public boolean resetPassword(String userName, String newPassword) {
        return motechAuthenticationService.resetPassword(userName, newPassword);
    }
}
