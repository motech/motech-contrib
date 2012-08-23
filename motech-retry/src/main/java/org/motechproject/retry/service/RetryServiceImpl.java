package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.event.MotechEvent;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.dao.AllRetriesDefinition;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRecord;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.retry.domain.RetryStatus;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.RepeatingSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.motechproject.retry.EventKeys.*;

@Service
public class RetryServiceImpl implements RetryService {
    public static final String RETRY_INTERNAL_SUBJECT = "org.motechproject.retry.internal";
    private MotechSchedulerService schedulerService;
    private AllRetries allRetries;
    private AllRetriesDefinition allRetriesDefinition;

    @Autowired
    public RetryServiceImpl(MotechSchedulerService schedulerService, AllRetries allRetries, AllRetriesDefinition allRetriesDefinition) {
        this.schedulerService = schedulerService;
        this.allRetries = allRetries;
        this.allRetriesDefinition = allRetriesDefinition;
    }

    public void schedule(RetryRequest retryRequest) {
        RetryRecord retryRecord = createNewRetryGroup(retryRequest);
        String externalId = retryRequest.getExternalId();
        String groupName = allRetriesDefinition.getRetryGroupName(retryRequest.getName());
        String retryName = retryRequest.getName();

        unscheduleRetryJob(externalId, groupName, retryName);
        final DateTime retryStartTime = retryRequest.getReferenceTime().plus(retryRecord.offset());
        schedulerService.scheduleRepeatingJob(new RepeatingSchedulableJob(motechEvent(retryRecord, retryRequest, jobIdKey(externalId, groupName, retryName)), retryStartTime.toDate(),
                endTime(retryStartTime, retryRecord.retryCount(), retryRecord.retryInterval()), intervalInMillis(retryRecord), false));
    }

    private RetryRecord createNewRetryGroup(RetryRequest retryRequest) {
        RetryRecord retryRecord = allRetriesDefinition.getRetryRecord(retryRequest.getName());
        allRetries.createRetry(new Retry(retryRequest.getName(), retryRequest.getExternalId(), retryRequest.getReferenceTime().plus(retryRecord.offset()), retryRecord.retryCount(), retryRecord.retryInterval()));
        return retryRecord;
    }

    protected boolean scheduleNextGroup(RetryRequest retryRequest) {
        RetryRecord nextRetryRecord = allRetriesDefinition.getNextRetryRecord(retryRequest.getName());
        boolean isLastRetryGroup = (null == nextRetryRecord);
        if (!isLastRetryGroup)
            schedule(new RetryRequest(nextRetryRecord.name(), retryRequest.getExternalId(), retryRequest.getReferenceTime()));
        return isLastRetryGroup;
    }

    public void unscheduleRetryGroup(String externalId, String name) {
        List<String> allRetryNames = allRetriesDefinition.getAllRetryRecordNames(name);
        for (String retryName : allRetryNames) {
            unscheduleRetryJob(externalId, name, retryName);
        }
    }

    private void unscheduleRetryJob(String externalId, String name, String retryName) {
        schedulerService.safeUnscheduleJob(RETRY_INTERNAL_SUBJECT, jobIdKey(externalId, name, retryName) + "-repeat");
    }

    public void fulfill(String externalId, String groupName) {
        List<String> retryRecordNames = allRetriesDefinition.getAllRetryRecordNames(groupName);
        for (String recordName : retryRecordNames) {
            Retry activeRetry = allRetries.getActiveRetry(externalId, recordName);
            if (activeRetry != null) {
                activeRetry.setRetryStatus(RetryStatus.COMPLETED);
                allRetries.update(activeRetry);
                unscheduleRetryGroup(externalId, groupName);
            }
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
            put(EXTERNAL_ID, retryRequest.getExternalId());
            put(NAME, retryRecord.name());
            put(REFERENCE_TIME, retryRequest.getReferenceTime());
            put(MotechSchedulerService.JOB_ID_KEY, jobId);
        }});
    }

    private String jobIdKey(String externalId, String groupName, String name) {
        return String.format("%s.%s.%s", externalId, groupName, name);
    }
}
