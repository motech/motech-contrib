package org.motechproject.http.client.listener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientEventListenerTest {

    @Mock
    private RestTemplate restTempate;

    @Mock
    HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory;

    private HttpClientEventListener httpClientEventListener;

    @Before
    public void setup() {
        when(restTempate.getRequestFactory()).thenReturn(httpComponentsClientHttpRequestFactory);
        httpClientEventListener = new HttpClientEventListener(restTempate);
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

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(restTempate).postForLocation(eq(postUrl), captor.capture());
        assertRequestObject(postData, captor.getValue());
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

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(restTempate).put(eq(putUrl), captor.capture());
        assertRequestObject(postData, captor.getValue());
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpDeleteCall() throws IOException {
        final String deleteUrl = "http://commcare";
        final String deleteRequest = "aragorn";
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, deleteUrl);
            put(EventDataKeys.DATA, deleteRequest);
            put(EventDataKeys.METHOD, Method.DELETE);
        }});

        httpClientEventListener.handle(motechEvent);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(restTempate).delete(eq(deleteUrl), captor.capture());
        assertRequestObject(deleteRequest, captor.getValue());
    }

    private void assertRequestObject(String expected, Object requestObject) {
        assertTrue(requestObject instanceof HttpEntity);
        assertEquals(expected, ((HttpEntity) requestObject).getBody());
    }
}