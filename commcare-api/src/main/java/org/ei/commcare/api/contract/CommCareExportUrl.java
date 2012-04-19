package org.ei.commcare.api.contract;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

public class CommCareExportUrl {
    private String base;
    private Map<String, String> queryParams;

    public CommCareExportUrl(String base, Map<String, String> queryParams) {
        this.base = base;
        this.queryParams = queryParams;
    }

    public String url(String previousToken) {
        return base + "?export_tag=%22" + nameSpace() + "%22&format=json&previous_export=" + previousToken;
    }

    public String nameSpace() {
        return queryParams.get("nameSpace");
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
