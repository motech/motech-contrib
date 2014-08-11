package org.motechproject.casexml.gateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.casexml.domain.CaseTask;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.agent.components.SynchronousCall;
import org.motechproject.http.agent.domain.EventDataKeys;
import org.motechproject.http.agent.domain.EventSubjects;

import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareCaseGatewayTest {

    @Mock
    private CaseTaskXmlConverter caseTaskXmlConverter;

    @Mock
    private SynchronousCall mockCommunicationType;

    private CommcareCaseGateway commcareCaseGateway;

    @Before
    public void before() {
        commcareCaseGateway = new CommcareCaseGateway(caseTaskXmlConverter, mockCommunicationType);
    }

    @Test
    public void shouldFormXmlRequestAndPostItToCommCare() {
        CaseTask task = new CaseTask();
        String data = "request";
        String url = "someUrl";
        when(caseTaskXmlConverter.convertToCaseXml(task)).thenReturn(data);
        commcareCaseGateway.submitCase(url, task, null, null, 3);
        verify(caseTaskXmlConverter).convertToCaseXml(task);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.DATA, data);
        parameters.put(EventDataKeys.METHOD, "POST");
        parameters.put(EventDataKeys.USERNAME, null);
        parameters.put(EventDataKeys.PASSWORD, null);
        verify(mockCommunicationType).send(new MotechEvent(EventSubjects.HTTP_REQUEST, parameters));
    }

    @Test
    public void shouldFormXmlRequestAndPostItToCommCareForCloseCase() {
        CaseTask task = new CaseTask();
        String data = "request";
        String url = "someUrl";
        when(caseTaskXmlConverter.convertToCloseCaseXml(task)).thenReturn(data);
        commcareCaseGateway.closeCase(url, task, null, null, 3);
        verify(caseTaskXmlConverter).convertToCloseCaseXml(task);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.DATA, data);
        parameters.put(EventDataKeys.METHOD, "POST");
        parameters.put(EventDataKeys.USERNAME, null);
        parameters.put(EventDataKeys.PASSWORD, null);
        verify(mockCommunicationType).send(new MotechEvent(EventSubjects.HTTP_REQUEST, parameters));
    }
}
