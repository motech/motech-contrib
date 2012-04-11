package org.motechproject.casexml.gateway;

import org.motechproject.casexml.domain.CaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CommcareCaseGateway{
    private CaseTaskXmlConverter caseTaskXmlConverter;
    private RestTemplate restTemplate;

    @Autowired
    public CommcareCaseGateway(CaseTaskXmlConverter caseTaskXmlConverter) {
        this(caseTaskXmlConverter, new RestTemplate());
    }

    public CommcareCaseGateway(CaseTaskXmlConverter caseTaskXmlConverter, RestTemplate restTemplate) {
        this.caseTaskXmlConverter = caseTaskXmlConverter;
        this.restTemplate = restTemplate;
    }

    public void submitCase(String commcareUrl, CaseTask task){
        String request = caseTaskXmlConverter.convertToCaseXmlWithEnvelope(task);
        restTemplate.postForLocation(commcareUrl, request);
    }
}
