package org.motechproject.retry.domain;

import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.ReadWritablePeriod;

import java.util.List;

import static org.motechproject.retry.util.PeriodParser.FORMATTER;

public class RetryRecord {
    private String name;
    private Integer retryCount;
    private List<String> retryInterval;

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
        ReadWritablePeriod period = new MutablePeriod();
        for (String interval : retryInterval) {
            period.add(parse(interval));
        }
        return period.toPeriod();
    }

    private Period parse(String interval) {
        return Period.parse(interval, FORMATTER);
    }

    public void setRetryInterval(List<String> retryInterval) {
        this.retryInterval = retryInterval;
    }
}
