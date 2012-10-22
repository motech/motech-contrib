package org.motechproject.diagnostics.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class DiagnosticConfiguration {

    @Autowired
    @Qualifier("diagnosticProperties")
    private Properties properties;

    public DiagnosticConfiguration() {
    }

    public String activeMqQueueNames() {
        return properties.getProperty("activeMQ.queueNames");
    }


    public String diagnosticServices(){
        return properties.getProperty("diagnosticServices");
    }

    public String contextPath(){
        return properties.getProperty("application.context.path");
    }

    public String logFileLocation(){
        return properties.getProperty("log.location");
    }

}


