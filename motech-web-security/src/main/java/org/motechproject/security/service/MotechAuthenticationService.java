package org.motechproject.security.service;

import org.motechproject.security.authentication.MotechPasswordEncoder;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
public class MotechAuthenticationService {

    @Autowired
    private AllMotechWebUsers allMotechWebUsers;

    @Autowired
    private MotechPasswordEncoder passwordEncoder;

    public void register(String username, String password, String externalId, List<String> roles) throws WebSecurityException {
        this.register(username, password, externalId, roles, true);
    }

    public void register(String username, String password, String externalId, List<String> roles, boolean isActive) throws WebSecurityException {
        validateUserInfo(username, password);
        password = passwordEncoder.encodePassword(password);
        MotechWebUser webUser = new MotechWebUser(username, password, externalId, roles);
        webUser.setActive(isActive);
        allMotechWebUsers.add(webUser);
    }

    private void validateUserInfo(String username, String password) throws WebSecurityException {
        if (isBlank(username) || isBlank(password)) {
            throw new WebSecurityException("Username or password cannot be empty");
        }
    }

    public void activateUser(String username) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(username);
        if (motechWebUser != null) {
            motechWebUser.setActive(true);
            allMotechWebUsers.update(motechWebUser);
        }
    }

    public MotechUser retrieveUserByCredentials(String username, String password) {
        MotechWebUser user = allMotechWebUsers.findByUserName(username);
        if (user != null && passwordEncoder.isPasswordValid(user.getPassword(), password)) {
            return new MotechUser(user);
        }
        return null;
    }

    public MotechUser changePassword(String username, String oldPassword, String newPassword) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(username);
        if (motechWebUser != null && passwordEncoder.isPasswordValid(motechWebUser.getPassword(), oldPassword)) {
            motechWebUser.setPassword(passwordEncoder.encodePassword(newPassword));
            allMotechWebUsers.update(motechWebUser);
            return new MotechUser(motechWebUser);
        }
        return null;
    }

    public void remove(String username) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(username);
        if (motechWebUser != null)
            allMotechWebUsers.remove(motechWebUser);
    }

    public boolean hasUser(String username) {
        return allMotechWebUsers.findByUserName(username) != null;
    }

    public List<MotechUser> findByRoles(String role) {
        List<MotechWebUser> result = allMotechWebUsers.findByRole(role);
        List<MotechUser> users = new ArrayList<MotechUser>();
        for(MotechWebUser user : result) {
            users.add(new MotechUser(user));
        }
        return users;
    }
}

