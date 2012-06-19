package org.ei.commcare.api.contract;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class CommCareModuleDefinitions {
    private String userName;
    private String password;
    private String caseFetchURLBase;
    private List<CommCareModuleDefinition> modules;

    public String userName() {
        return userName;
    }

    public String password() {
        return password;
    }

    public List<CommCareModuleDefinition> modules() {
        return modules;
    }

    public String caseFetchURLBase() {
        return caseFetchURLBase;
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
