package org.motechproject.excel.builder.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ReportQueries {

    private Properties properties;

    @Autowired
    public ReportQueries(@Qualifier("reportQueryProperty") Properties properties) {
        this.properties = properties;
    }

    public String getQuery(String reportType) {
        return properties.getProperty(reportType);
    }
}
