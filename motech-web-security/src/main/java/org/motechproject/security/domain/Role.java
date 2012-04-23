package org.motechproject.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

public class Role {

    private final String name;

    public Role(String name) {
        this.name = name;
    }

    public GrantedAuthority authority() {
        return new GrantedAuthorityImpl(name);
    }
}
