package org.motechproject.retry.domain;

import org.joda.time.DateTime;

public class RetryRequest {
    private final String name;
    private final String externalId;
    private DateTime startTime;

    public RetryRequest(String name, String externalId, DateTime startTime) {
        this.name = name;
        this.externalId = externalId;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public String getExternalId() {
        return externalId;
    }

    public DateTime getStartTime() {
        return startTime;
    }
}
