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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RetryRequest that = (RetryRequest) o;

        if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (referenceTime != null ? !referenceTime.equals(that.referenceTime) : that.referenceTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
        result = 31 * result + (referenceTime != null ? referenceTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RetryRequest{" +
                "name='" + name + '\'' +
                ", externalId='" + externalId + '\'' +
                ", referenceTime=" + referenceTime +
                '}';
    }
}
