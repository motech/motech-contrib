package org.ei.commcare.api.contract;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

public class CommCareFormDefinition {
    private CommCareExportUrl url;
    private String name;
    private final Map<String, String> mappings;

    public CommCareFormDefinition(String name, CommCareExportUrl url, Map<String, String> mappings) {
        this.url = url;
        this.name = name;
        this.mappings = mappings;
    }

    public String url(String previousToken) {
        return url.url(previousToken);
    }

    public String name() {
        return name;
    }

    public Map<String, String> mappings() {
        return mappings;
    }

    public String nameSpace() {
        return url.nameSpace();
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
