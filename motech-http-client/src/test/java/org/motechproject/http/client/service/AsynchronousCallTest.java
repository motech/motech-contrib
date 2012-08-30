package org.motechproject.http.client.service;

import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.EventRelay;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.components.AsynchronousCall;
import org.motechproject.http.client.listener.HttpClientEventListener;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AsynchronousCallTest {

    @Mock
    private EventRelay mockEventRelay;
    @Mock
    private HttpClientEventListener mockHttpEventListener;

    private AsynchronousCall asynchronousCall;

    @Test
    public void shouldSendThroughEventRelay() throws Exception {
        initMocks(this);
        asynchronousCall = new AsynchronousCall(mockEventRelay);
        MotechEvent motechEvent = new MotechEvent("subject");
        asynchronousCall.send(motechEvent);

        verify(mockEventRelay).sendEventMessage(motechEvent);

    }
}
