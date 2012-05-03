package org.motechproject.security.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@TypeDiscriminator("doc.type == 'MotechWebUser'")
public class MotechWebUser extends MotechBaseDataObject {

    @JsonProperty
    private String externalId;

    @JsonProperty
    private String userName;

    @JsonProperty
    private String password;

    @JsonProperty
    private List<Role> roles;

    public MotechWebUser(){
        super();
    }

    public MotechWebUser(String userName, String password, String externalId, List<Role> roles) {
        super();
        this.userName = userName;
        this.password = password;
        this.externalId = externalId;
        if(roles != null) {
            this.roles = roles;
        }
    }

    public String getExternalId() {
        return externalId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Role role : roles) {
            authorities.add(role.authority());
        }
        return authorities;
    }
}
