package org.motechproject.retry.domain;

import java.util.List;

public class RetryJsonRecord {
    private String groupName;
    private List<RetryRecord> retries;

    public List<RetryRecord> getRetries() {
        return retries;
    }

    public void setRetries(List<RetryRecord> retries) {
        this.retries = retries;
    }

    public String getName() {
        return groupName;
    }

    public void setName(String name) {
        this.groupName = name;
    }
}
