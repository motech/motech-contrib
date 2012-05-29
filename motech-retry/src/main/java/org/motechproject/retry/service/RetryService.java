package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.model.MotechEvent;
import org.motechproject.model.RepeatingSchedulableJob;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRecord;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.retry.domain.RetryStatus;
import org.motechproject.scheduler.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
        RetryRecord retryRecord = createNewRetry(retryRequest);
        String externalId = retryRequest.getExternalId();
        String groupName = allRetries.getRetryGroupName(retryRequest.getName());
        String retryName = retryRequest.getName();

        unscheduleRetryJob(externalId, groupName, retryName);
        schedulerService.scheduleRepeatingJob(new RepeatingSchedulableJob(motechEvent(retryRecord, retryRequest, jobIdKey(externalId, groupName, retryName)), retryRequest.getStartTime().toDate(),
                endTime(retryRequest.getStartTime(), retryRecord.retryCount(), retryRecord.retryInterval()), retryRecord.retryCount(), intervalInMillis(retryRecord)));
    }

    private RetryRecord createNewRetry(RetryRequest retryRequest) {
        RetryRecord retryRecord = allRetries.getRetryRecord(retryRequest.getName());
        allRetries.createRetry(new Retry(retryRequest.getName(), retryRequest.getExternalId(), retryRequest.getStartTime(), retryRecord.retryCount(), retryRecord.retryInterval()));
        return retryRecord;
    }

    protected void scheduleNext(RetryRequest retryRequest) {
        RetryRecord nextRetryRecord = allRetries.getNextRetryRecord(retryRequest.getName());
        schedule(new RetryRequest(nextRetryRecord.name(), retryRequest.getExternalId(), retryRequest.getReferenceTime(), retryRequest.getReferenceTime()));
    }

    public void unscheduleRetryGroup(String externalId, String name) {
        List<String> allRetryNames = allRetries.getAllRetryRecordNames(name);
        for (String retryName : allRetryNames) {
            unscheduleRetryJob(externalId, name, retryName);
        }
    }

    private void unscheduleRetryJob(String externalId, String name, String retryName) {
        schedulerService.safeUnscheduleJob(RETRY_INTERNAL_SUBJECT, jobIdKey(externalId, name, retryName) + "-repeat");
    }

    public void fulfill(String externalId, String name) {
        Retry activeRetry = allRetries.getActiveRetry(externalId, name);
        if (activeRetry != null) {
            activeRetry.setRetryStatus(RetryStatus.COMPLETED);
            allRetries.update(activeRetry);
            String groupName = allRetries.getRetryGroupName(name);
            unscheduleRetryJob(externalId, groupName, name);
        }
    }

    private Date endTime(DateTime startTime, Integer repeatCount, Period retryInterval) {
        return startTime.plus(retryInterval.toStandardDuration().getMillis() * repeatCount).toDate();
    }

    private long intervalInMillis(RetryRecord retryRecord) {
        return (retryRecord.retryInterval().toStandardDuration().getMillis());
    }

    private MotechEvent motechEvent(final RetryRecord retryRecord, final RetryRequest retryRequest, final String jobId) {
        return new MotechEvent(RETRY_INTERNAL_SUBJECT, new HashMap<String, Object>() {{
            String externalId = retryRequest.getExternalId();
            String name = retryRecord.name();

            put(EXTERNAL_ID, externalId);
            put(NAME, name);
            put(MAX_RETRY_COUNT, retryRecord.retryCount());
            put(RETRY_INTERVAL, retryRecord.retryInterval());
            put(START_TIME, retryRequest.getStartTime());
            put(REFERENCE_TIME, retryRequest.getReferenceTime());
            put(MotechSchedulerService.JOB_ID_KEY, jobId);
        }});
    }

    private String jobIdKey(String externalId, String groupName, String name) {
        return String.format("%s.%s.%s", externalId, groupName, name);
    }
}
