package org.motechproject.security.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.model.MotechBaseDataObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

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
    private List<String> roles;

    @JsonProperty
    private boolean active = true;

    public MotechWebUser() {
        super();
    }

    public MotechWebUser(String userName, String password, String externalId, List<String> roles) {
        super();
        this.userName = userName == null ? null : userName.toLowerCase();
        this.password = password;
        this.externalId = externalId;
        this.roles = roles;
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

    public List<String> getRoles() {
        return roles;
    }

    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : roles) {
            authorities.add(new GrantedAuthorityImpl(role));
        }
        return authorities;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MotechWebUser that = (MotechWebUser) o;

        if (active != that.active) return false;
        if (!externalId.equals(that.externalId)) return false;
        if (!roles.equals(that.roles)) return false;
        if (!userName.equals(that.userName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = externalId.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + roles.hashCode();
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
