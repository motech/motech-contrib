package org.motechproject.diagnostics.configuration;

import java.util.ArrayList;
import java.util.List;

public class Link {
    String name;
    String url;

    List<Link> links = new ArrayList<>();

    public Link(String name) {
        this.name = name;
    }

    public Link(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void add(Link link) {
        links.add(link);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Link)) return false;

        Link link = (Link) o;

        if (links != null ? !links.equals(link.links) : link.links != null) return false;
        if (name != null ? !name.equals(link.name) : link.name != null) return false;
        if (url != null ? !url.equals(link.url) : link.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        return result;
    }
}
