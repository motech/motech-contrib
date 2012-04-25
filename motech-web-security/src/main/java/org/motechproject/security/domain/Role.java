package org.motechproject.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

public class Role {

    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public GrantedAuthority authority() {
        return new GrantedAuthorityImpl(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
