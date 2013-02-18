package db.migration.domain;

import org.apache.commons.lang.StringUtils;

public class Report {

    private String name;
    private EntityResource resource;

    public Report(String name, EntityResource resource) {
        this.name = name;
        this.resource = resource;
    }

    public Report(EntityResource resource) {
        this(StringUtils.EMPTY, resource);
    }

    public Report(String name) {
        this(name, null);
    }

    public String getName() {
        return name;
    }

    public EntityResource getResource() {
        return resource;
    }
}