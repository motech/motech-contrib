package org.motechproject.retry.domain;

import org.joda.time.DateTime;

public class RetryRequest {
    private final String name;
    private final String externalId;
    private DateTime startTime;
    private DateTime referenceTime;

    public RetryRequest(String name, String externalId, DateTime startTime, DateTime referenceTime) {
        this.name = name;
        this.externalId = externalId;
        this.startTime = startTime;
        this.referenceTime = referenceTime;
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

    public DateTime getReferenceTime() {
        return referenceTime;
    }
}
