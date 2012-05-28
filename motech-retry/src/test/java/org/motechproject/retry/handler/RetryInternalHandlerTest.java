package org.motechproject.retry.handler;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.gateway.OutboundEventGateway;
import org.motechproject.model.MotechEvent;
import org.motechproject.retry.EventKeys;
import org.motechproject.retry.dao.AllRetries;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryStatus;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RetryInternalHandlerTest {
    private RetryInternalHandler retryInternalHandler;

    @Mock
    private AllRetries mockAllRetries;
    @Mock
    private OutboundEventGateway mockOutboundGateway;

    @Before
    public void setUp() {
        initMocks(this);
        retryInternalHandler = new RetryInternalHandler(mockAllRetries, mockOutboundGateway);
    }

    @Test
    public void shouldMakeRetryToBeInactiveWhenMaxRetriesReached() {
        final String externalId = "externalId";
        final String name = "name";
        final MotechEvent event = new MotechEvent("someSubject", new HashMap<String, Object>() {{
            put(EventKeys.EXTERNAL_ID, externalId);
            put(EventKeys.NAME, name);
        }});

        Retry retry = new Retry(name, externalId, DateTime.now(), 0, Period.millis(600));
        when(mockAllRetries.getActiveRetry(externalId, name)).thenReturn(retry);

        retryInternalHandler.handle(event);

        assertThat(retry.retryStatus(), is(RetryStatus.INACTIVE));

        assertMotechEvent(true);
    }

    @Test
    public void shouldNotMakeRetryToBeInactiveAndDecrementTheRetriesLeft() {
        final String externalId = "externalId";
        final String name = "name";
        final MotechEvent event = new MotechEvent("someSubject", new HashMap<String, Object>() {{
            put(EventKeys.EXTERNAL_ID, externalId);
            put(EventKeys.NAME, name);
        }});

        Retry retry = new Retry(name, externalId, DateTime.now(), 2, Period.millis(600));
        when(mockAllRetries.getActiveRetry(externalId, name)).thenReturn(retry);

        retryInternalHandler.handle(event);

        assertThat(retry.retryStatus(), is(RetryStatus.ACTIVE));
        assertThat(retry.retriesLeft(), is(1));

        assertMotechEvent(false);
    }

    private void assertMotechEvent(boolean isLastEvent) {
        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockOutboundGateway).sendEventMessage(eventCaptor.capture());
        MotechEvent motechEvent = eventCaptor.getValue();
        assertThat(motechEvent.getSubject(), is(EventKeys.RETRY_SUBJECT));
        assertThat(motechEvent.isLastEvent(), is(isLastEvent));
    }
}
