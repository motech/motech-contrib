package org.motechproject.retry.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.model.MotechBaseDataObject;

import static org.motechproject.retry.util.PeriodParser.FORMATTER;
import static org.motechproject.util.DateUtil.setTimeZone;

@TypeDiscriminator("doc.type === 'Retry'")
public class Retry extends MotechBaseDataObject {
    @JsonProperty
    private String name;
    @JsonProperty
    private String externalId;
    @JsonProperty
    private DateTime startTime;
    @JsonProperty
    private Integer retriesLeft;
    @JsonProperty
    private String retryInterval;
    @JsonProperty
    private RetryStatus retryStatus;

    public Retry(String name, String externalId, DateTime startTime, Integer retriesLeft, Period retryInterval) {
        this.name = name;
        this.externalId = externalId;
        this.startTime = startTime;
        this.retriesLeft = retriesLeft;
        this.retryInterval = retryInterval.toString(FORMATTER);
        this.retryStatus = RetryStatus.ACTIVE;
    }

    public Retry() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public void setRetriesLeft(Integer retriesLeft) {
        this.retriesLeft = retriesLeft;
    }

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }

    public Period retryInterval() {
        return Period.parse(this.retryInterval, FORMATTER);
    }

    public DateTime startTime() {
        return setTimeZone(startTime);
    }

    public String externalId() {
        return externalId;
    }

    public String name() {
        return name;
    }

    public void setRetryStatus(RetryStatus retryStatus) {
        this.retryStatus = retryStatus;
    }
}
