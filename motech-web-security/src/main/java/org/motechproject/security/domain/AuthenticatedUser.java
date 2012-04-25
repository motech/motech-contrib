package org.motechproject.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class AuthenticatedUser extends User {

    private MotechWebUser webUser;

    public AuthenticatedUser(List<GrantedAuthority> authorities, MotechWebUser user) {
        super(user.getUserName(), user.getPassword(), true, true, true, true, authorities);
        this.webUser = user;
    }

    public String getId() {
        return webUser.getId();
    }

    public String getExternalId() {
        return webUser.getExternalId();
    }

    public String getUserType() {
        return webUser.getUserType();
    }
}
