package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.dao.AllRetriesDefinition;
import org.motechproject.retry.domain.*;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.MotechEvent;
import org.motechproject.scheduler.domain.RepeatingSchedulableJob;
import org.motechproject.util.DateUtil;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.retry.EventKeys.*;
import static org.motechproject.retry.service.RetryServiceImpl.RETRY_INTERNAL_SUBJECT;
import static org.motechproject.retry.util.PeriodParser.FORMATTER;

public class RetryServiceImplTest {
    private RetryServiceImpl retryServiceImpl;
    @Mock
    private MotechSchedulerService mockSchedulerService;
    @Mock
    private AllRetries mockAllRetries;
    @Mock
    private AllRetriesDefinition mockAllRetriesDef;

    @Before
    public void setUp() {
        initMocks(this);
        retryServiceImpl = new RetryServiceImpl(mockSchedulerService, mockAllRetries, mockAllRetriesDef);
    }

    @Test
    public void shouldUnscheduleAndCreateRetrySchedule() {
        final String name = "retry-schedule-name";
        final String externalId = "uniqueExternalId";
        final DateTime referenceTime = DateTime.now();
        final String groupName = "groupName";
        final String offset = "1 hour";

        RetryRecord retryRecord = retryRecord(name, 2, asList("2 hours"), offset);
        when(mockAllRetriesDef.getRetryRecord(name)).thenReturn(retryRecord);
        RetryGroupRecord retryGroupRecord = new RetryGroupRecord();
        retryGroupRecord.setName(groupName);
        when(mockAllRetriesDef.getRetryGroup(name)).thenReturn(retryGroupRecord);

        retryServiceImpl.schedule(new RetryRequest(name, externalId, referenceTime));

        ArgumentCaptor<RepeatingSchedulableJob> jobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(mockSchedulerService).safeUnscheduleJob(RETRY_INTERNAL_SUBJECT, externalId + "." + groupName + "." + name + "-repeat");
        verify(mockSchedulerService).scheduleRepeatingJob(jobCaptor.capture());

        RepeatingSchedulableJob actualJob = jobCaptor.getValue();
        assertThat(actualJob.getMotechEvent(), is(new MotechEvent(RETRY_INTERNAL_SUBJECT, new HashMap<String, Object>() {{
            put(EXTERNAL_ID, externalId);
            put(NAME, name);
            put(REFERENCE_TIME, referenceTime);
            put(MotechSchedulerService.JOB_ID_KEY, externalId + "." + groupName + "." + name);
        }})));

        final Period expectedOffset = Period.hours(1);
        assertThat(actualJob.getStartTime(), is(referenceTime.plus(expectedOffset).toDate()));
        assertThat(actualJob.getEndTime(), is(referenceTime.plus(expectedOffset).plusHours(4).toDate()));
        assertThat(actualJob.getRepeatCount(), is(2));
        assertThat(actualJob.getRepeatIntervalInMilliSeconds(), is(Period.parse("2 hours", FORMATTER).toStandardDuration().getMillis()));
    }

    @Test
    public void shouldScheduleNextRetry() {
        DateTime referenceTime = DateTime.now();
        String externalId = "externalId";
        String name = "retrySchedule1";

        RetryRecord nextRetryRecord = new RetryRecord();
        nextRetryRecord.setName("retrySchedule2");

        when(mockAllRetriesDef.getNextRetryRecord(name)).thenReturn(nextRetryRecord);

        RetryServiceImpl service = spy(retryServiceImpl);
        doNothing().when(service).schedule(Matchers.<RetryRequest>any());
        service.scheduleNextGroup(new RetryRequest(name, externalId, referenceTime));

        ArgumentCaptor<RetryRequest> requestCaptor = ArgumentCaptor.forClass(RetryRequest.class);
        verify(service).schedule(requestCaptor.capture());

        RetryRequest request = requestCaptor.getValue();
        assertThat(request.getName(), is("retrySchedule2"));
        assertThat(request.getReferenceTime(), is(referenceTime));
        assertThat(request.getExternalId(), is(externalId));
    }

    @Test
    public void shouldNotScheduleNextRetryIfNoRetryRecordsArePresent() {
        DateTime referenceTime = DateTime.now();
        String externalId = "externalId";
        String name = "retrySchedule1";

        RetryRecord nextRetryRecord = new RetryRecord();
        nextRetryRecord.setName("retrySchedule2");
        nextRetryRecord.setRetryInterval(asList("1 Day"));
        nextRetryRecord.setRetryCount(4);

        when(mockAllRetriesDef.getNextRetryRecord(name)).thenReturn(null);

        RetryServiceImpl service = spy(retryServiceImpl);
        doNothing().when(service).schedule(Matchers.<RetryRequest>any());
        service.scheduleNextGroup(new RetryRequest(name, externalId, referenceTime));

        verify(service, never()).schedule(Matchers.<RetryRequest>any());
    }

    @Test
    public void shouldFulFillExistingActiveSchedule() {
        final String name = "retry-schedule-name";
        final String externalId = "uniqueExternalId";
        DateTime startTime = DateUtil.now();

        Retry retry = new Retry(name, externalId, startTime, 0, Period.days(1));
        retry.setRetryStatus(RetryStatus.ACTIVE);
        when(mockAllRetriesDef.getAllRetryRecordNames("groupName")).thenReturn(asList(name));
        when(mockAllRetries.getActiveRetry(externalId, name)).thenReturn(retry);

        retryServiceImpl.fulfill(externalId, "groupName");

        assertThat(retry.retryStatus(), is(RetryStatus.COMPLETED));
    }

    private RetryRecord retryRecord(String name, int retryCount, List<String> retryInterval, String offset) {
        RetryRecord retryRecord = new RetryRecord();
        retryRecord.setName(name);
        retryRecord.setRetryCount(retryCount);
        retryRecord.setRetryInterval(retryInterval);
        retryRecord.setOffset(offset);
        return retryRecord;
    }
}
