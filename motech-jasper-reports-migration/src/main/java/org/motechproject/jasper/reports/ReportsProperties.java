package org.motechproject.jasper.reports;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReportsProperties {

    private Properties properties;

    public ReportsProperties() {
        properties = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/jasperReports.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getReportsSourceLocation() {
        return properties.getProperty("reports.source.location");
    }

    public String getReportsRootLocation() {
        return properties.getProperty("jasper.reports.root.location.name");
    }

    public String getJasperServerResourceURL() {
        String resourcePath = properties.getProperty("jasper.resource.path");
        return String.format("%s%s", getJasperServerURL(), resourcePath);
    }

    public String getJasperServerUserName() {
        return properties.getProperty("jasper.username");
    }

    public String getJasperServerPassword() {
        return properties.getProperty("jasper.password");
    }

    public String getJasperRoleCreationURL() {
        String roleCreationPath = properties.getProperty("jasper.role.creation.path");
        return String.format("%s%s", getJasperServerURL(), roleCreationPath);
    }

    private String getJasperServerURL() {
        return properties.getProperty("jasper.url");
    }

    public String getJasperPermissionsURL() {
        String setPermissionsPath = properties.getProperty("jasper.set.permissions.path");
        return String.format("%s%s", getJasperServerURL(), setPermissionsPath);
    }
}