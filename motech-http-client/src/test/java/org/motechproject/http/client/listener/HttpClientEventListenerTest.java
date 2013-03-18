package org.motechproject.http.client.listener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.event.EventRelay;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientEventListenerTest {

    @Mock
    private RestTemplate restTempate;
    @Mock
    private EventRelay eventRelay;

    private HttpClientEventListener httpClientEventListener;

    @Before
    public void setUp() {
        initMocks(this);
        httpClientEventListener = new HttpClientEventListener(restTempate, eventRelay);
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCallForPost() throws IOException {
        final String postUrl = "http://commcare";
        final String postData = "aragorn";
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, postUrl);
            put(EventDataKeys.DATA, postData);
            put(EventDataKeys.METHOD, Method.POST);
        }});

        httpClientEventListener.handle(motechEvent);

        ArgumentCaptor<HttpEntity> entityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTempate).postForLocation(eq(postUrl), entityArgumentCaptor.capture());

        HttpEntity entity = entityArgumentCaptor.getValue();
        assertEquals(postData, entity.getBody());
        assertEquals(new HttpHeaders(), entity.getHeaders());
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCallForPostWithHeaders() throws IOException {
        final String postUrl = "http://commcare";
        final String postData = "aragorn";
        final HashMap<String, String> headerParams = new HashMap();
        headerParams.put("api-key", "api-key-value");
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, postUrl);
            put(EventDataKeys.DATA, postData);
            put(EventDataKeys.HEADERS, headerParams);
            put(EventDataKeys.METHOD, Method.POST);
        }});

        httpClientEventListener.handle(motechEvent);

        ArgumentCaptor<HttpEntity> entityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTempate).postForLocation(eq(postUrl), entityArgumentCaptor.capture());
        HttpEntity entity = entityArgumentCaptor.getValue();
        assertEquals(postData, entity.getBody());
        assertEquals(getHttpHeaders(headerParams), entity.getHeaders());
    }

    @Test
    public void shouldFireCallbackEvent() {
        final String postUrl = "http://commcare";
        final String postData = "aragorn";

        final EventCallBack eventCallBack = mock(EventCallBack.class);
        MotechEvent expectedCallbackEvent = new MotechEvent("subject");
        when(eventCallBack.getCallBackEvent()).thenReturn(expectedCallbackEvent);

        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, postUrl);
            put(EventDataKeys.DATA, postData);
            put(EventDataKeys.METHOD, Method.POST);
            put(EventDataKeys.CALLBACK, eventCallBack);
        }});

        httpClientEventListener.handle(motechEvent);

        ArgumentCaptor<HttpEntity> entityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTempate).postForLocation(eq(postUrl), entityArgumentCaptor.capture());
        verify(eventRelay).sendEventMessage(expectedCallbackEvent);
    }

    private MultiValueMap<String, String> getHttpHeaders(HashMap<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for(String param : headers.keySet()){
            httpHeaders.add(param, headers.get(param));
        }
        return httpHeaders;
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCall() throws IOException {
        final String putUrl = "http://commcare";
        final String postData = "aragorn";
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, putUrl);
            put(EventDataKeys.DATA, postData);
            put(EventDataKeys.METHOD, Method.PUT);
        }});

        httpClientEventListener.handle(motechEvent);

        ArgumentCaptor<HttpEntity> entityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTempate).put(eq(putUrl), entityArgumentCaptor.capture());

        HttpEntity entity = entityArgumentCaptor.getValue();
        assertEquals(postData, entity.getBody());
        assertEquals(new HttpHeaders(), entity.getHeaders());
    }
}
