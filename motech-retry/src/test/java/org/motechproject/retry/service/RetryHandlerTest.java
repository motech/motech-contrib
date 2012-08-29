package org.motechproject.retry.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.OutboundEventGateway;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.dao.AllRetriesDefinition;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryGroupRecord;
import org.motechproject.retry.domain.RetryRequest;
import org.motechproject.retry.domain.RetryStatus;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.retry.EventKeys.*;

public class RetryHandlerTest {
    private RetryHandler retryHandler;

    @Mock
    private AllRetries mockAllRetries;
    @Mock
    private OutboundEventGateway mockOutboundGateway;
    @Mock
    private RetryServiceImpl mockRetryServiceImpl;
    @Mock
    private AllRetriesDefinition mockAllRetriesDefinitions;

    @Before
    public void setUp() {
        initMocks(this);
        retryHandler = new RetryHandler(mockAllRetries, mockAllRetriesDefinitions, mockOutboundGateway, mockRetryServiceImpl);
    }

    @Test
    public void shouldMakeRetryToBeInactiveWhenMaxRetriesReached() {
        final String externalId = "externalId";
        final String name = "name";
        final DateTime referenceTime = DateTime.now();
        String retryEventSubject = "group-subject";
        final MotechEvent event = new MotechEvent("someSubject", new HashMap<String, Object>() {{
            put(EXTERNAL_ID, externalId);
            put(NAME, name);
            put(REFERENCE_TIME, referenceTime);
        }});

        Retry retry = new Retry(name, externalId, DateTime.now(), 0, Period.millis(600));
        RetryGroupRecord retryGroupRecord = new RetryGroupRecord();
        retryGroupRecord.setEventSubject(retryEventSubject);
        when(mockAllRetriesDefinitions.getRetryGroup(name)).thenReturn(retryGroupRecord);
        when(mockAllRetries.getActiveRetry(externalId, name)).thenReturn(retry);
        when(mockRetryServiceImpl.scheduleNextGroup(Matchers.<RetryRequest>any())).thenReturn(true);

        retryHandler.handle(event);

        assertThat(retry.retryStatus(), is(RetryStatus.DEFAULTED));

        assertMotechEvent(true, retryEventSubject);

        ArgumentCaptor<RetryRequest> retryRequestCaptor = ArgumentCaptor.forClass(RetryRequest.class);
        verify(mockRetryServiceImpl).scheduleNextGroup(retryRequestCaptor.capture());
        RetryRequest request = retryRequestCaptor.getValue();

        assertThat(request.getName(), is(name));
        assertThat(request.getReferenceTime(), is(referenceTime));
    }

    @Test
    public void shouldNotMakeRetryToBeInactiveAndDecrementTheRetriesLeft() {
        final String externalId = "externalId";
        final String name = "name";
        String retryEventSubject = "group-subject";
        final MotechEvent event = new MotechEvent("someSubject", new HashMap<String, Object>() {{
            put(EXTERNAL_ID, externalId);
            put(NAME, name);
        }});

        Retry retry = new Retry(name, externalId, DateTime.now(), 2, Period.millis(600));
        RetryGroupRecord retryGroupRecord = new RetryGroupRecord();
        retryGroupRecord.setEventSubject(retryEventSubject);
        when(mockAllRetriesDefinitions.getRetryGroup(name)).thenReturn(retryGroupRecord);
        when(mockAllRetries.getActiveRetry(externalId, name)).thenReturn(retry);

        retryHandler.handle(event);

        assertThat(retry.retryStatus(), is(RetryStatus.ACTIVE));
        assertThat(retry.retriesLeft(), is(1));

        assertMotechEvent(false, retryEventSubject);
    }

    private void assertMotechEvent(boolean isLastEvent, String subject) {
        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockOutboundGateway).sendEventMessage(eventCaptor.capture());
        MotechEvent motechEvent = eventCaptor.getValue();
        assertThat(motechEvent.getSubject(), is(subject));
        assertThat(motechEvent.isLastEvent(), is(isLastEvent));
    }
}
