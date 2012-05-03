package org.motechproject.security.domain;

import java.util.ArrayList;
import java.util.Collection;

public class Roles extends ArrayList<Role> {
    public Roles() {
    }

    public Roles(Collection<? extends Role> roles) {
        super(roles);
    }

    public boolean hasRole(String roleName) {
        for (Role role : this) {
            if (role.getName().equalsIgnoreCase(roleName))
                return true;
        }
        return false;
    }
}
