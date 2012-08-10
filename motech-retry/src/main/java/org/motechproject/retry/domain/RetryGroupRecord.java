package org.motechproject.retry.domain;

import java.util.List;

public class RetryGroupRecord {
    private String groupName;
    private List<RetryRecord> retries;
    private String eventSubject;

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

    public void setEventSubject(String eventSubject) {
        this.eventSubject = eventSubject;
    }

    public String getEventSubject() {
        return eventSubject;
    }
}
