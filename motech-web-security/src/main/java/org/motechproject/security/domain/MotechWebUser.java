package org.motechproject.security.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'Provider'")
public class MotechWebUser extends CouchDbDocument {

    @JsonProperty
    private String id;

    @JsonProperty
    private String userName;

    @JsonProperty
    private String password;

    @JsonProperty
    private List<Role> roles;

    protected MotechWebUser(String id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        roles = new ArrayList<Role>();
    }

    public String getId() {
        return id;
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

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : roles) {
            authorities.add(role.authority());
        }
        return authorities;
    }
}
