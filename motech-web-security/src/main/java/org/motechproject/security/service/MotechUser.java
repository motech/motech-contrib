package org.motechproject.security.service;

import org.motechproject.security.domain.MotechWebUser;

import java.util.List;

public class MotechUser {

    private MotechWebUser user;

    public MotechUser(MotechWebUser user) {
        this.user = user;
    }

    public String getExternalId() {
        return user.getExternalId();
    }

    public String getUserName() {
        return user.getUserName();
    }

    public List<String> getRoles() {
        return user.getRoles();
    }
    public boolean isActive() {
        return user.isActive();
    }
}
