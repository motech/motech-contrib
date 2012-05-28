package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.model.MotechEvent;
import org.motechproject.model.RepeatingSchedulableJob;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRecord;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.scheduler.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

import static org.motechproject.retry.EventKeys.*;

@Service
public class RetryService {
    public static final String RETRY_INTERNAL_SUBJECT = "org.motechproject.retry.internal";
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
        schedulerService.safeUnscheduleJob(RETRY_INTERNAL_SUBJECT, retryRequest.getExternalId());
        schedulerService.scheduleRepeatingJob(new RepeatingSchedulableJob(motechEvent(retryRecord, retryRequest), retryRequest.getStartTime().toDate(),
                endTime(retryRequest.getStartTime(), retryRecord.retryCount(), retryRecord.retryInterval()), retryRecord.retryCount(), intervalInMillis(retryRecord)));
    }

    private Date endTime(DateTime startTime, Integer repeatCount, Period retryInterval) {
        return startTime.plus(retryInterval.toStandardDuration().getMillis() * repeatCount).toDate();
    }

    private long intervalInMillis(RetryRecord retryRecord) {
        return (retryRecord.retryInterval().toStandardDuration().getMillis());
    }

    private MotechEvent motechEvent(final RetryRecord retryRecord, final RetryRequest retryRequest) {
        return new MotechEvent(RETRY_INTERNAL_SUBJECT, new HashMap<String, Object>() {{
            String externalId = retryRequest.getExternalId();
            String name = retryRecord.name();

            put(EXTERNAL_ID, externalId);
            put(NAME, name);
            put(MAX_RETRY_COUNT, retryRecord.retryCount());
            put(RETRY_INTERVAL, retryRecord.retryInterval());
            put(MotechSchedulerService.JOB_ID_KEY, jobIdKey(externalId, name));
        }});
    }

    private String jobIdKey(String externalId, String name) {
        return String.format("%s.%s", externalId, name);
    }

}
