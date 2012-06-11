package org.motechproject.security.service;

import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
public class MotechAuthenticationService {

    @Autowired
    private AllMotechWebUsers allMotechWebUsers;
    @Autowired
    private MotechPasswordEncoder passwordEncoder;

    public void register(String userName, String password, String externalId, List<String> roles) throws WebSecurityException {
        this.register(userName, password, externalId, roles, true);
    }

    public void register(String userName, String password, String externalId, List<String> roles, boolean isActive) throws WebSecurityException {
        validateUserInfo(userName, password);
        password = passwordEncoder.encodePassword(password);
        MotechWebUser webUser = new MotechWebUser(userName, password, externalId, roles);
        webUser.setActive(isActive);
        allMotechWebUsers.add(webUser);
    }

    private void validateUserInfo(String userName, String password) throws WebSecurityException {
        if (isBlank(userName) || isBlank(password)) {
            throw new WebSecurityException("Username or password cannot be empty");
        }
    }

    public void activateUser(String userName) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if (motechWebUser != null) {
            motechWebUser.setActive(true);
            allMotechWebUsers.update(motechWebUser);
        }
    }

    public MotechWebUser changePassword(String userName, String newPassword) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if (motechWebUser == null)
            return null;
        motechWebUser.setPassword(passwordEncoder.encodePassword(newPassword));
        allMotechWebUsers.update(motechWebUser);
        return motechWebUser;
    }

    public void remove(String userName) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if (motechWebUser != null)
            allMotechWebUsers.remove(motechWebUser);
    }
}

