package org.motechproject.casexml.gateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.casexml.domain.CaseTask;
import org.motechproject.http.client.components.CommunicationType;
import org.motechproject.http.client.domain.EventDataKeys;
import org.motechproject.http.client.domain.EventSubjects;
import org.motechproject.http.client.domain.Method;
import org.motechproject.http.client.service.HttpClientServiceImpl;
import org.motechproject.event.MotechEvent;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareCaseGatewayTest {

    @Mock
    private CaseTaskXmlConverter caseTaskXmlConverter;

    @Mock
    private CommunicationType mockCommunicationType;

    private CommcareCaseGateway commcareCaseGateway;

    @Before
    public void before() {
        HttpClientServiceImpl httpClientService = new HttpClientServiceImpl();
        ReflectionTestUtils.setField(httpClientService, "communicationType", mockCommunicationType);
        commcareCaseGateway = new CommcareCaseGateway(caseTaskXmlConverter, httpClientService);
    }

    @Test
    public void shouldFormXmlRequestAndPostItToCommCare() {
        CaseTask task = new CaseTask();
        String data = "request";
        String url = "someUrl";
        when(caseTaskXmlConverter.convertToCaseXml(task)).thenReturn(data);
        commcareCaseGateway.submitCase(url, task);
        verify(caseTaskXmlConverter).convertToCaseXml(task);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(EventDataKeys.HEADERS, null);
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.DATA, data);
        parameters.put(EventDataKeys.METHOD, Method.POST);
        verify(mockCommunicationType).send(new MotechEvent(EventSubjects.HTTP_REQUEST, parameters));
    }

    @Test
    public void shouldFormXmlRequestAndPostItToCommCareForCloseCase() {
        CaseTask task = new CaseTask();
        String data = "request";
        String url = "someUrl";
        when(caseTaskXmlConverter.convertToCloseCaseXml(task)).thenReturn(data);
        commcareCaseGateway.closeCase(url, task);
        verify(caseTaskXmlConverter).convertToCloseCaseXml(task);
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(EventDataKeys.HEADERS, null);
        parameters.put(EventDataKeys.URL, url);
        parameters.put(EventDataKeys.DATA, data);
        parameters.put(EventDataKeys.METHOD, Method.POST);
        verify(mockCommunicationType).send(new MotechEvent(EventSubjects.HTTP_REQUEST, parameters));
    }
}
