package org.motechproject.retry.domain;

import java.util.List;

public class RetryJsonRecord {
    private String name;
    private List<RetryRecord> retries;

    public List<RetryRecord> getRetries() {
        return retries;
    }

    public void setRetries(List<RetryRecord> retries) {
        this.retries = retries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
