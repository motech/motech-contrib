package db.migration.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PermissionItem {
    @XmlAttribute(name = "xsi:type")
    private String xsiType;
    @XmlAttribute(name = "xmlns:xsi")
    private String xmlnsXsi;

    @XmlElement
    private Integer permissionMask;
    @XmlElement
    private PermissionRecipient permissionRecipient;
    @XmlElement
    private String URI;


    public PermissionItem() {
        this.xsiType = "objectPermissionImpl";
        this.xmlnsXsi = "http://www.w3.org/2001/XMLSchema-instance";
    }

    public PermissionItem(Integer permissionMask, PermissionRecipient permissionRecipient, String URI) {
        this();
        this.permissionMask = permissionMask;
        this.permissionRecipient = permissionRecipient;
        this.URI = URI;
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
