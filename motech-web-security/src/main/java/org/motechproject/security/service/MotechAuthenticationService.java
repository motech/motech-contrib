package org.motechproject.security.service;

import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.domain.Role;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MotechAuthenticationService {

    private AllMotechWebUsers allMotechWebUsers;

    @Autowired
    public MotechAuthenticationService(AllMotechWebUsers allMotechWebUsers) {
        this.allMotechWebUsers = allMotechWebUsers;
    }

    public void register(String userName, String password, String externalId, List<String> roles) {
        List<Role> rolesDomain = new ArrayList<Role>();
        for (String role : roles) {
            rolesDomain.add(new Role(role));
        }
        allMotechWebUsers.add(new MotechWebUser(userName, password, externalId, rolesDomain));
    }

    public void remove(String userName) {
        MotechWebUser motechWebUser = allMotechWebUsers.findByUserName(userName);
        if(motechWebUser != null)
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
