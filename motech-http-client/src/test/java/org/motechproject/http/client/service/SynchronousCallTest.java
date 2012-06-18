package org.motechproject.http.client.service;

import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.http.client.components.SynchronousCall;
import org.motechproject.http.client.listener.HttpClientEventListener;
import org.motechproject.scheduler.domain.MotechEvent;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SynchronousCallTest {

    private SynchronousCall synchronousCall;
    @Mock
    private HttpClientEventListener mockHttpClientEventListener;

    @Test
    public void shouldInvokeHttpClientEventListenerDirectly() {
        initMocks(this);
        synchronousCall = new SynchronousCall(mockHttpClientEventListener);
        MotechEvent motechEvent = new MotechEvent("subject");
        synchronousCall.send(motechEvent);
        verify(mockHttpClientEventListener).handle(motechEvent);

    }

}
