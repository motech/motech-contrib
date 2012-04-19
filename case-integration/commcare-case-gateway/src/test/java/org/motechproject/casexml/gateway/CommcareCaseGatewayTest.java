package org.motechproject.casexml.gateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.casexml.domain.CaseTask;
import org.motechproject.event.EventRelay;
import org.motechproject.http.client.constants.EventDataKeys;
import org.motechproject.http.client.constants.EventSubjects;
import org.motechproject.http.client.service.HttpClientServiceImpl;
import org.motechproject.model.MotechEvent;

import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareCaseGatewayTest  {

    @Mock
    private CaseTaskXmlConverter caseTaskXmlConverter;

    @Mock
    private EventRelay eventRelay;

    private CommcareCaseGateway commcareCaseGateway;

    @Before
    public void before(){
        commcareCaseGateway = new CommcareCaseGateway(caseTaskXmlConverter, new HttpClientServiceImpl(eventRelay) );
    }

    @Test
    public void shouldFormXmlRequestAndPostItToCommCare(){
        CaseTask task = new CaseTask();
        String data = "request";
        String url = "someUrl";
        when(caseTaskXmlConverter.convertToCaseXmlWithEnvelope(task)).thenReturn(data);
        commcareCaseGateway.submitCase(url, task);
        verify(caseTaskXmlConverter).convertToCaseXmlWithEnvelope(task);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.DATA, data);
        verify(eventRelay).sendEventMessage(new MotechEvent(EventSubjects.HTTP_REQUEST,parameters));
    }
}
