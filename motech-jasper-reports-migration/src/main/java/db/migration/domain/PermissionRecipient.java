package db.migration.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "permissionRecipient")
public class PermissionRecipient {
    @XmlAttribute(name = "xsi:type")
    private String xsiType;
    @XmlElement
    private Boolean externallyDefined;
    @XmlElement
    private String name;

    public PermissionRecipient() {
        this.xsiType = "roleImpl";
        this.externallyDefined = false;
    }

    public PermissionRecipient(String name) {
        this();
        this.name = name;
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
