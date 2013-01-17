package db.migration.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "role")
public class Role {
    @XmlElement
    private Boolean externallyDefined;
    @XmlElement
    private String roleName;

    public Role() {
    }

    public Role(String roleName) {
        this.externallyDefined = false;
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
