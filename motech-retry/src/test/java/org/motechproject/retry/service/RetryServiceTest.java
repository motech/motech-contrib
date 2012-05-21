package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.model.MotechEvent;
import org.motechproject.model.RunOnceSchedulableJob;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.RetryRecord;
import org.motechproject.retry.domain.RetryRequest;
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
    public void shouldCreateRetrySchedule() {
        String name = "retry-schedule-name";
        String externalId = "uniqueExternalId";
        DateTime startTime = DateUtil.now();

        RetryRecord retryRecord = retryRecord(name, 2, asList("2 hours"));
        when(mockAllRetries.getRetryRecord(name)).thenReturn(retryRecord);

        retryService.schedule(new RetryRequest(name, externalId, startTime));

        ArgumentCaptor<RunOnceSchedulableJob> jobCaptor = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
        verify(mockSchedulerService).scheduleRunOnceJob(jobCaptor.capture());
        RunOnceSchedulableJob actualJob = jobCaptor.getValue();
        assertThat(actualJob.getMotechEvent(), is(new MotechEvent(RetryService.RETRY_SUBJECT, new HashMap<String, Object>() {{
            put(RetryService.MAX_RETRY_COUNT, 2);
            put(RetryService.RETRY_INTERVAL, Period.parse("2 hours", FORMATTER));
        }})));
        assertThat(actualJob.getStartDate(), is(startTime.toDate()));
    }

    private RetryRecord retryRecord(String name, int retryCount, List<String> retryInterval) {
        RetryRecord retryRecord = new RetryRecord();
        retryRecord.setName(name);
        retryRecord.setRetryCount(retryCount);
        retryRecord.setRetryInterval(retryInterval);
        return retryRecord;
    }

}
