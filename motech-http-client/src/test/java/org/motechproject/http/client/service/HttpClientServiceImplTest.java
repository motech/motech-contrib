package org.motechproject.http.client.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.components.CommunicationType;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.Method;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class HttpClientServiceImplTest {

    @Mock
    private CommunicationType mockCommunicationType;

    HttpClientService httpClientService;

    @Before
    public void setup() {
        initMocks(this);
        httpClientService = new HttpClientServiceImpl();
        ReflectionTestUtils.setField(httpClientService, "communicationType", mockCommunicationType);
    }

    @Test
    public void shouldInvokeSendOfCommunicationType() {
        String url = "someurl";
        String data = "data";
        httpClientService.post(url, data);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockCommunicationType).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.POST, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
    }

    @Test
    public void shouldInvokePutRequests() {
        String url = "someurl";
        String data = "data";
        httpClientService.put(url, data);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockCommunicationType).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.PUT, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
    }

    @Test
    public void shouldInvokePostsWithHeaderParams() {
        String url = "someurl";
        String data = "data";
        HashMap<String, String> headers  = new HashMap<>();
        headers.put("api-key", "api-key-value");

        httpClientService.post(url, data, headers);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockCommunicationType).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.POST, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
        assertEquals(headers, eventMessageSent.getParameters().get(EventDataKeys.HEADERS));

    }
}
