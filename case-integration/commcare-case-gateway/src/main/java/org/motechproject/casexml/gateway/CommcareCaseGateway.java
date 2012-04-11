package org.motechproject.casexml.gateway;

import org.motechproject.casexml.domain.CaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Component
public class CommcareCaseGateway{
    private CaseTaskXmlConverter caseTaskXmlConverter;
    private Properties commcareProperties;
    private RestTemplate restTemplate;

    @Autowired
    public CommcareCaseGateway(CaseTaskXmlConverter caseTaskXmlConverter, @Qualifier("commcareProperties") Properties properties) {
        this(caseTaskXmlConverter, properties, new RestTemplate());
    }

    public CommcareCaseGateway(CaseTaskXmlConverter caseTaskXmlConverter, Properties properties, RestTemplate restTemplate) {
        this.caseTaskXmlConverter = caseTaskXmlConverter;
        commcareProperties = properties;
        this.restTemplate = restTemplate;
    }

    public void submitCase(CaseTask task){
        String request = caseTaskXmlConverter.convertToCaseXmlWithEnvelope(task);
        String url = commcareProperties.getProperty("commcare.hq.url");
        restTemplate.postForLocation(url, request);
    }
}
