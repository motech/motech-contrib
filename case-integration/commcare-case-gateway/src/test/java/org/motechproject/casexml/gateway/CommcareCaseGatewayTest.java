package org.motechproject.casexml.gateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.casexml.domain.CaseTask;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareCaseGatewayTest  {

    @Mock
    private CaseTaskXmlConverter caseTaskXmlConverter;

    @Mock
    protected RestTemplate restTemplate;

    private CommcareCaseGateway commcareCaseGateway;

    @Before
    public void before(){
        commcareCaseGateway = new CommcareCaseGateway(caseTaskXmlConverter, restTemplate );
    }

    @Test
    public void shouldFormXmlRequestAndPostItToCommCare(){
        CaseTask task = new CaseTask();
        when(caseTaskXmlConverter.convertToCaseXmlWithEnvelope(task)).thenReturn("request");
        commcareCaseGateway.submitCase("someUrl", task);
        verify(caseTaskXmlConverter).convertToCaseXmlWithEnvelope(task);
        verify(restTemplate).postForLocation("someUrl", "request");
    }
}
