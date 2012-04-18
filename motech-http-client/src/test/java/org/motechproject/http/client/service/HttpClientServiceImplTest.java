package org.motechproject.http.client.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.context.EventContext;
import org.motechproject.event.EventRelay;
import org.motechproject.http.client.constants.EventDataKeys;
import org.motechproject.model.MotechEvent;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventContext.class)
public class HttpClientServiceImplTest {

    @Mock
    private EventContext eventContext;

    @Mock
    private EventRelay eventRelay;

    @Before
    public void setup() {
        initMocks(this);

        PowerMockito.mockStatic(EventContext.class);
        when(EventContext.getInstance()).thenReturn(eventContext);
        when(eventContext.getEventRelay()).thenReturn(eventRelay);
    }
    @Test
    public void ShouldRaiseAnEventForHttpSend() {
        String url = "someurl";
        String data = "somedata";
        new HttpClientServiceImpl(eventRelay).post(url, data);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(eventRelay).sendEventMessage(motechEventArgumentCaptor.capture());

        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
    }
}
