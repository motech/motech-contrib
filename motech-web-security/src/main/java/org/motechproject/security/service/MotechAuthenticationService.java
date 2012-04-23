package org.motechproject.security.service;

import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MotechAuthenticationService {

    private AllMotechWebUsers allMotechWebUsers;

    @Autowired
    public MotechAuthenticationService(AllMotechWebUsers allMotechWebUsers) {
        this.allMotechWebUsers = allMotechWebUsers;
    }

    public void register(MotechWebUser user) {
        allMotechWebUsers.add(user);
    }

    public MotechWebUser findByUserName(String userName) {
       return allMotechWebUsers.findByUserName(userName);
    }

    public void remove(MotechWebUser user) {
        allMotechWebUsers.remove(user);
    }

    public AuthenticatedUser authenticate(String userName, String password) {
        MotechWebUser user = allMotechWebUsers.findByUserName(userName);
        if (user != null && password.equals(user.getPassword())) {
            return new AuthenticatedUser(user.getAuthorities(), user);
        }
        return null;
    }
}
