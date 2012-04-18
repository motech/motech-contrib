package org.motechproject.http.client.listener;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.http.client.constants.EventDataKeys;
import org.motechproject.http.client.constants.EventSubjects;
import org.motechproject.model.MotechEvent;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientEventListenerTest {

    @Mock
    private RestTemplate restTempate;

    @Test
    public void shouldReadFromQueueAndMakeAHttpCall() throws IOException {
        
        final String postUrl = "http://commcare";
        final String postData = "aragorn";
        MotechEvent motechEvent = new MotechEvent(EventSubjects.HTTP_REQUEST, new HashMap<String, Object>() {{
            put(EventDataKeys.URL, postUrl);
            put(EventDataKeys.DATA, postData);
        }});
        new HttpClientEventListener(restTempate).handle(motechEvent);
        Mockito.verify(restTempate).postForLocation(postUrl, postData);
    }
}
