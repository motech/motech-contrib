package org.motechproject.casexml.gateway;

import org.motechproject.casexml.domain.CaseTask;
import org.motechproject.http.client.service.HttpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommcareCaseGateway{
    private CaseTaskXmlConverter caseTaskXmlConverter;
    private HttpClientService httpClientService;

    @Autowired
    public CommcareCaseGateway(CaseTaskXmlConverter caseTaskXmlConverter, HttpClientService httpClientService) {
        this.caseTaskXmlConverter = caseTaskXmlConverter;
        this.httpClientService = httpClientService;
    }

    public void submitCase(String commcareUrl, CaseTask task){
        String request = caseTaskXmlConverter.convertToCaseXmlWithEnvelope(task);
        httpClientService.post(commcareUrl,request);
    }
}
