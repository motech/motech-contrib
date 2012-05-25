package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.motechproject.model.MotechEvent;
import org.motechproject.model.RepeatingSchedulableJob;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRecord;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.scheduler.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;

public class RetryService {
    public static final String RETRY_SUBJECT = "org.motechproject.retry";
    public static final String MAX_RETRY_COUNT = "MAX_RETRY_COUNT";
    public static final String RETRY_INTERVAL = "RETRY_INTERVAL";
    private MotechSchedulerService schedulerService;
    private AllRetries allRetries;

    @Autowired
    public RetryService(MotechSchedulerService schedulerService, AllRetries allRetries) {
        this.schedulerService = schedulerService;
        this.allRetries = allRetries;
    }

    public void schedule(RetryRequest retryRequest) {
        RetryRecord retryRecord = allRetries.getRetryRecord(retryRequest.getName());
        allRetries.createRetry(new Retry(retryRequest.getName(), retryRequest.getExternalId(), retryRequest.getStartTime(), retryRecord.retryCount(), retryRecord.retryInterval()));
        schedulerService.safeUnscheduleJob(RETRY_SUBJECT, retryRequest.getExternalId());
        schedulerService.scheduleRepeatingJob(new RepeatingSchedulableJob(motechEvent(retryRecord), retryRequest.getStartTime().toDate(),
                endTime(retryRequest.getStartTime(), retryRecord.retryCount(), retryRecord.retryInterval()), retryRecord.retryCount(), intervalInMillis(retryRecord)));
    }

    private Date endTime(DateTime startTime, Integer repeatCount, Period retryInterval) {
        Duration standardDuration = retryInterval.toStandardDuration();
        return startTime.plus(standardDuration.getMillis() * repeatCount).toDate();
    }

    private long intervalInMillis(RetryRecord retryRecord) {
        return (retryRecord.retryInterval().toStandardDuration().getMillis());
    }

    private MotechEvent motechEvent(final RetryRecord retryRecord) {
        return new MotechEvent(RETRY_SUBJECT, new HashMap<String, Object>() {{
            put(MAX_RETRY_COUNT, retryRecord.retryCount());
            put(RETRY_INTERVAL, retryRecord.retryInterval());
        }});
    }

}
