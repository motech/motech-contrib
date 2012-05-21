package org.motechproject.retry.domain;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatterBuilder;

public class RetryRecord {
    private String name;
    private Integer retryCount;
    private String retryInterval;

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer retryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Period retryInterval() {
        return Period.parse(retryInterval, new PeriodFormatterBuilder()
                .appendYears().appendSuffix(" year", " years")
                .appendMonths().appendSuffix(" month", " months")
                .appendDays().appendSuffix(" day", " days")
                .appendHours().appendSuffix(" hour", " hours")
                .appendMinutes().appendSuffix(" minute", " minutes")
                .appendSeconds().appendSuffix(" second", " seconds")
                .toFormatter());
    }

    public void setRetryInterval(String retryInterval) {
        this.retryInterval = retryInterval;
    }
}
