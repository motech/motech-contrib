package org.motechproject.http.client.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.components.CommunicationType;
import org.motechproject.http.client.components.SynchronousCall;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.Method;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class HttpClientServiceImplTest {

    @Mock
    private CommunicationType mockCommunicationType;
    @Mock
    private SynchronousCall synchronousCall;

    HttpClientService httpClientService;

    @Before
    public void setup() {
        initMocks(this);
        httpClientService = new HttpClientServiceImpl();
        ReflectionTestUtils.setField(httpClientService, "communicationType", mockCommunicationType);
        ReflectionTestUtils.setField(httpClientService, "synchronousCall", synchronousCall);
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
    public void shouldExecuteRequestsBasedOnMethod() {
        String url = "someurl";
        String data = "data";
        httpClientService.execute(url, data, Method.POST);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockCommunicationType).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.POST, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
    }

    @Test
    public void shouldExecuteSynchronousCalls() {
        String url = "someurl";
        String data = "data";

        httpClientService.executeSync(url, data, Method.POST);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(synchronousCall).send(motechEventArgumentCaptor.capture());
        MotechEvent eventMessageSent = motechEventArgumentCaptor.getValue();

        assertEquals(Method.POST, eventMessageSent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));
    }

    @Test
    public void shouldAddAPIHeadersWithPostWithHeaders() {
        Map<String, String> headers = new HashMap<>();
        String apiKeyValue = "12345";
        String data = "data";
        String url = "url";
        String key = "api-key";
        headers.put(key, apiKeyValue);

        httpClientService.post(url, data, headers);

        ArgumentCaptor<MotechEvent> motechEventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockCommunicationType).send(motechEventCaptor.capture());
        MotechEvent motechEvent = motechEventCaptor.getValue();
        Map<String, Object> parameters = motechEvent.getParameters();
        HashMap<String, String> actualHeaders = (HashMap<String, String>) parameters.get(EventDataKeys.HEADERS);

        assertEquals(apiKeyValue, actualHeaders.get(key));
        assertEquals(Method.POST, parameters.get(EventDataKeys.METHOD));
        assertEquals(data, (String) parameters.get(EventDataKeys.DATA));
        assertEquals(url, parameters.get(EventDataKeys.URL));
    }

    @Test
    public void shouldAddAPIHeadersWithPutWithHeaders() {
        Map<String, String> headers = new HashMap<>();
        String data = "data";
        String url = "url";
        String key = "api-key";
        String apiKeyValue = "12345";
        headers.put(key, apiKeyValue);

        httpClientService.put(url, data, headers);

        ArgumentCaptor<MotechEvent> motechEventCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockCommunicationType).send(motechEventCaptor.capture());
        MotechEvent motechEvent = motechEventCaptor.getValue();
        Map<String, Object> parameters = motechEvent.getParameters();
        HashMap<String, String> actualHeaders = (HashMap<String, String>) parameters.get(EventDataKeys.HEADERS);

        assertEquals(apiKeyValue, actualHeaders.get(key));
        assertEquals(Method.PUT, parameters.get(EventDataKeys.METHOD));
        assertEquals(data, (String) parameters.get(EventDataKeys.DATA));
        assertEquals(url, parameters.get(EventDataKeys.URL));
    }

    @Test
    public void shouldAddAPIHeadersForExecuteRequests() {
        Map<String, String> headers = new HashMap<>();
        String url = "someurl";
        String data = "data";
        String key = "api-key";
        String apiKeyValue = "12345";
        headers.put(key, apiKeyValue);

        httpClientService.execute(url, data, Method.POST, headers);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(mockCommunicationType).send(motechEventArgumentCaptor.capture());
        MotechEvent motechEvent = motechEventArgumentCaptor.getValue();
        Map<String, Object> parameters = motechEvent.getParameters();
        HashMap<String, String> actualHeaders = (HashMap<String, String>) parameters.get(EventDataKeys.HEADERS);

        assertEquals(apiKeyValue, actualHeaders.get(key));
        assertEquals(Method.POST, motechEvent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) motechEvent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, motechEvent.getParameters().get(EventDataKeys.URL));
    }

    @Test
    public void shouldExecuteSynchronousCallsWithHeaders() {
        Map<String, String> headers = new HashMap<>();
        String url = "someurl";
        String data = "data";
        String key = "api-key";
        String apiKeyValue = "1234";
        headers.put(key, apiKeyValue);

        httpClientService.executeSync(url, data, Method.POST, headers);

        ArgumentCaptor<MotechEvent> motechEventArgumentCaptor = ArgumentCaptor.forClass(MotechEvent.class);
        verify(synchronousCall).send(motechEventArgumentCaptor.capture());
        MotechEvent motechEvent = motechEventArgumentCaptor.getValue();
        Map<String, Object> parameters = motechEvent.getParameters();
        HashMap<String, String> actualHeaders = (HashMap<String, String>) parameters.get(EventDataKeys.HEADERS);

        assertEquals(apiKeyValue, actualHeaders.get(key));
        assertEquals(Method.POST, motechEvent.getParameters().get(EventDataKeys.METHOD));
        assertEquals(data, (String) motechEvent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, motechEvent.getParameters().get(EventDataKeys.URL));
    }
}
