package org.motechproject.retry.domain;

import org.joda.time.DateTime;

public class RetryRequest {
    private final String name;
    private final String externalId;
    private DateTime referenceTime;

    public RetryRequest(String name, String externalId, DateTime referenceTime) {
        this.name = name;
        this.externalId = externalId;
        this.referenceTime = referenceTime;
    }

    public String getName() {
        return name;
    }

    public String getExternalId() {
        return externalId;
    }

    public DateTime getReferenceTime() {
        return referenceTime;
    }
}
