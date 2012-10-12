package org.motechproject.caselogs.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class CaseLogConfiguration {

    @Autowired
    @Qualifier("caseLogProperties")
    private Properties properties;

    @Value("#{caseLogProperties['case.logs.limit']}")
    private Integer caseLogsLimit;

    public CaseLogConfiguration() {
    }

    public String contextPath() {
        return properties.getProperty("application.context.path");
    }

    public Integer getCaseLogsLimit() {
        return caseLogsLimit;
    }
}


