package org.motechproject.security.service;

import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.domain.Role;
import org.motechproject.security.domain.Roles;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
public class MotechAuthenticationService {

    private AllMotechWebUsers allMotechWebUsers;

    @Autowired
    public MotechAuthenticationService(AllMotechWebUsers allMotechWebUsers) {
        this.allMotechWebUsers = allMotechWebUsers;
    }

    public void register(String userName, String password, String externalId, List<String> roles) throws WebSecurityException {
        validateUserInfo(userName, password);
        Roles rolesDomain = new Roles();
        for (String role : roles) {
            rolesDomain.add(new Role(role));
        }
        allMotechWebUsers.add(new MotechWebUser(userName, password, externalId, rolesDomain));
    }

    private void validateUserInfo(String userName, String password) throws WebSecurityException {
        if(isBlank(userName) || isBlank(password)) {
            throw new WebSecurityException("Username or password cannot be empty");
        }
    }

    public void register(String userName, String password, String externalId, List<String> roles, boolean isActive) throws WebSecurityException {
        validateUserInfo(userName, password);

        Roles rolesDomain = new Roles();
        for (String role : roles) {
            rolesDomain.add(new Role(role));
        }
        MotechWebUser user = new MotechWebUser(userName, password, externalId, rolesDomain);
        user.setActive(isActive);
        allMotechWebUsers.add(user);
    }

    public void activateUser(String userName) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if (null != motechWebUser) {
            motechWebUser.setActive(true);
            allMotechWebUsers.update(motechWebUser);
        }
    }

    public AuthenticatedUser changePassword(String userName, String newPassword) {
        allMotechWebUsers.changePassword(userName, newPassword);
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if (motechWebUser == null)
            return null;
        return new AuthenticatedUser(motechWebUser.getAuthorities(), motechWebUser);
    }

    public void remove(String userName) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if (motechWebUser != null)
            allMotechWebUsers.remove(motechWebUser);
    }

    public AuthenticatedUser authenticate(String userName, String password) {
        MotechWebUser user = allMotechWebUsers.findByUserName(userName);
        if (user != null && password.equals(user.getPassword())) {
            return new AuthenticatedUser(user.getAuthorities(), user);
        }
        return null;
    }
}
