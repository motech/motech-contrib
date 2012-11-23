package org.motechproject.caselogs.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class CaseLogConfiguration {

    @Autowired
    @Qualifier("caseLogProperties")
    private Properties properties;

    public CaseLogConfiguration() {
    }

    public String contextPath(){
        return properties.getProperty("application.context.path");
    }
}


