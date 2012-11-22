package org.motechproject.http.client.listener;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientEventListenerTest {

    @Mock
    private RestTemplate restTempate;

    @Test
    public void shouldReadFromQueueAndMakeAHttpCallForPost() throws IOException {
        final String postUrl = "http://commcare";
        final String postData = "aragorn";
        final HashMap<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("api-key", "1234");
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, postUrl);
            put(EventDataKeys.DATA, postData);
            put(EventDataKeys.METHOD, Method.POST);
            put(EventDataKeys.HEADERS, expectedHeaders);
        }});

        new HttpClientEventListener(restTempate).handle(motechEvent);

        ArgumentCaptor<HttpEntity> requestEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTempate).exchange(eq(postUrl), eq(HttpMethod.POST), requestEntityCaptor.capture(), eq(Object.class));

        HttpEntity value = requestEntityCaptor.getValue();
        HttpHeaders actualHeaders = value.getHeaders();
        assertEquals(expectedHeaders.get("api-key"),actualHeaders.get("api-key").get(0));
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCallWithoutHttpHeaders() throws IOException {
        final String putUrl = "http://commcare";
        final String postData = "aragorn";
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, putUrl);
            put(EventDataKeys.DATA, postData);
            put(EventDataKeys.METHOD, Method.PUT);
        }});

        new HttpClientEventListener(restTempate).handle(motechEvent);

        verify(restTempate).exchange(eq(putUrl), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class));
    }

    @Test
    public void shouldReadFromQueueAndMakeAHttpCall() throws IOException {
        final String putUrl = "http://commcare";
        final String postData = "aragorn";
        final HashMap<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put("api-key", "1234");
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, putUrl);
            put(EventDataKeys.DATA, postData);
            put(EventDataKeys.METHOD, Method.PUT);
            put(EventDataKeys.HEADERS, expectedHeaders);
        }});

        new HttpClientEventListener(restTempate).handle(motechEvent);

        ArgumentCaptor<HttpEntity> requestEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTempate).exchange(eq(putUrl), eq(HttpMethod.PUT), requestEntityCaptor.capture(), eq(Object.class));

        HttpEntity value = requestEntityCaptor.getValue();
        HttpHeaders actualHeaders = value.getHeaders();
        assertEquals(expectedHeaders.get("api-key"), actualHeaders.get("api-key").get(0));
    }
}
