package org.motechproject.security.service;

import org.motechproject.security.domain.MotechWebUser;
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
        allMotechWebUsers.add(new MotechWebUser(userName, password, externalId, roles));
    }

    private void validateUserInfo(String userName, String password) throws WebSecurityException {
        if(isBlank(userName) || isBlank(password)) {
            throw new WebSecurityException("Username or password cannot be empty");
        }
    }

    public void register(String userName, String password, String externalId, List<String> roles, boolean isActive) throws WebSecurityException {
        validateUserInfo(userName, password);
        MotechWebUser webUser = new MotechWebUser(userName, password, externalId, roles);
        webUser.setActive(isActive);
        allMotechWebUsers.add(webUser);
    }

    public void activateUser(String userName) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if (null != motechWebUser) {
            motechWebUser.setActive(true);
            allMotechWebUsers.update(motechWebUser);
        }
    }

    public MotechWebUser changePassword(String userName, String newPassword) {
        allMotechWebUsers.changePassword(userName, newPassword);
        return allMotechWebUsers.findByUserName(userName);
    }

    public void remove(String userName) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if (motechWebUser != null)
            allMotechWebUsers.remove(motechWebUser);
    }
}
