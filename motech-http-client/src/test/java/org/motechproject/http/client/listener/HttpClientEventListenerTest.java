package org.motechproject.http.client.listener;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.motechproject.model.MotechEvent;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientEventListenerTest {

    @Mock
    private RestTemplate restTempate;

    @Test
    public void shouldReadFromQueueAndMakeAHttpCallForPost() throws IOException {
        final String postUrl = "http://commcare";
        final String postData = "aragorn";
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, postUrl);
            put(EventDataKeys.DATA, postData);
            put(EventDataKeys.METHOD, Method.POST);
        }});
        new HttpClientEventListener(restTempate).handle(motechEvent);
        verify(restTempate).postForLocation(postUrl, postData);
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

        new HttpClientEventListener(restTempate).handle(motechEvent);

        verify(restTempate).put(putUrl, postData);
    }
}
