package db.migration.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "entityResource")
public class EntityResource {
    @XmlElement(name = "Item")
    private List<Item> item;

    public EntityResource() {
    }

    public EntityResource(List<Item> item) {
        this.item = item;
    }

    public List<Item> getItem() {
        return item;
    }
}
