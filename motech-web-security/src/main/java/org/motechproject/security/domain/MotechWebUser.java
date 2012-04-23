package org.motechproject.security.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'MotechUser'")
public class MotechWebUser extends CouchDbDocument {

    @JsonProperty
    private String id;

    @JsonProperty
    private String userName;

    @JsonProperty
    private String password;

    protected MotechWebUser(String name, String userName, String password, List<Role> roles) {
        this.userName = userName;
        this.password = password;
        if(roles != null) {
            this.roles = roles;
        }
    }
    private List<Role> roles;

    public MotechWebUser(String id, String userName, String password) {
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
