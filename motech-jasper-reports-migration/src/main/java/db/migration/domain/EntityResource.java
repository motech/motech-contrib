package db.migration.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "entityResource")
public class EntityResource {
    @XmlElement(name = "Item")
    private List<PermissionItem> permissionItem;

    public EntityResource() {
    }

    public EntityResource(List<PermissionItem> permissionItem) {
        this.permissionItem = permissionItem;
    }

    public List<PermissionItem> getPermissionItem() {
        return permissionItem;
    }
}
