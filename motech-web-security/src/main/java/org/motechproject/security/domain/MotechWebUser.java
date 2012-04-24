package org.motechproject.security.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'MotechUser'")
public class MotechWebUser extends CouchDbDocument {

    @JsonProperty
    private String name;

    @JsonProperty
    private String userName;

    @JsonProperty
    private String password;

    @JsonProperty
    private List<Role> roles = new ArrayList<Role>();

    protected MotechWebUser(String name, String userName, String password, List<Role> roles) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        if(roles != null) {
            this.roles = roles;
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public MotechWebUser addRole(Role role) {
        roles.add(role);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : roles) {
            authorities.add(role.authority());
        }
        return authorities;
    }
}
