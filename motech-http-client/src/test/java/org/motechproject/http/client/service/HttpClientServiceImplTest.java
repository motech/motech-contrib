package org.motechproject.http.client.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.http.client.components.CommunicationType;
import org.motechproject.http.client.constants.EventDataKeys;
import org.motechproject.scheduler.domain.MotechEvent;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

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

        assertEquals(data, (String) eventMessageSent.getParameters().get(EventDataKeys.DATA));
        assertEquals(url, eventMessageSent.getParameters().get(EventDataKeys.URL));

    }

}
