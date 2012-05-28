package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.model.MotechEvent;
import org.motechproject.model.RepeatingSchedulableJob;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRecord;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.retry.domain.RetryStatus;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.util.DateUtil;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.retry.EventKeys.*;
import static org.motechproject.retry.service.RetryService.RETRY_INTERNAL_SUBJECT;
import static org.motechproject.retry.util.PeriodParser.FORMATTER;

public class RetryServiceTest {
    private RetryService retryService;
    @Mock
    private MotechSchedulerService mockSchedulerService;
    @Mock
    private AllRetries mockAllRetries;

    @Before
    public void setUp() {
        initMocks(this);
        retryService = new RetryService(mockSchedulerService, mockAllRetries);
    }

    @Test
    public void shouldUnscheduleAndCreateRetrySchedule() {
        final String name = "retry-schedule-name";
        final String externalId = "uniqueExternalId";
        DateTime startTime = DateUtil.now();

        RetryRecord retryRecord = retryRecord(name, 2, asList("2 hours"));
        when(mockAllRetries.getRetryRecord(name)).thenReturn(retryRecord);

        retryService.schedule(new RetryRequest(name, externalId, startTime));

        ArgumentCaptor<RepeatingSchedulableJob> jobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(mockSchedulerService).safeUnscheduleJob(RETRY_INTERNAL_SUBJECT, externalId);
        verify(mockSchedulerService).scheduleRepeatingJob(jobCaptor.capture());

        RepeatingSchedulableJob actualJob = jobCaptor.getValue();
        assertThat(actualJob.getMotechEvent(), is(new MotechEvent(RETRY_INTERNAL_SUBJECT, new HashMap<String, Object>() {{
            put(MAX_RETRY_COUNT, 2);
            put(RETRY_INTERVAL, Period.parse("2 hours", FORMATTER));
            put(EXTERNAL_ID, externalId);
            put(NAME, name);
            put(MotechSchedulerService.JOB_ID_KEY, externalId + "." + name);
        }})));

        assertThat(actualJob.getStartTime(), is(startTime.toDate()));
        assertThat(actualJob.getEndTime(), is(startTime.plusHours(4).toDate()));
        assertThat(actualJob.getRepeatCount(), is(2));
        assertThat(actualJob.getRepeatInterval(), is(Period.parse("2 hours", FORMATTER).toStandardDuration().getMillis()));
    }

    @Test
    public void shouldFulFillExistingActiveSchedule() {
        final String name = "retry-schedule-name";
        final String externalId = "uniqueExternalId";
        DateTime startTime = DateUtil.now();

        Retry retry = new Retry(name, externalId, startTime, 0, Period.days(1));
        retry.setRetryStatus(RetryStatus.ACTIVE);
        when(mockAllRetries.getActiveRetry(externalId, name)).thenReturn(retry);

        retryService.fulfill(externalId, name);

        assertThat(retry.retryStatus(), is(RetryStatus.COMPLETED));
    }


    private RetryRecord retryRecord(String name, int retryCount, List<String> retryInterval) {
        RetryRecord retryRecord = new RetryRecord();
        retryRecord.setName(name);
        retryRecord.setRetryCount(retryCount);
        retryRecord.setRetryInterval(retryInterval);
        return retryRecord;
    }
}
