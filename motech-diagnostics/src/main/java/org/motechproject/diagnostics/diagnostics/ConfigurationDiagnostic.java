package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

@Component
public class ConfigurationDiagnostic {

    private HashMap<String, Properties> propertyFilesMap;

    @Autowired(required = false)
    public void setPropertyFilesMap(HashMap<String, Properties> propertyFilesMap) {
        this.propertyFilesMap = propertyFilesMap;
    }

    @Diagnostic(name = "configuration")
    public DiagnosticsResult performDiagnosis() throws JMSException {
        if(propertyFilesMap == null) return null;
        DiagnosticLog diagnosticLog = new DiagnosticLog("CONFIGURATION");

        for ( Map.Entry<String, Properties> propertiesMap : propertyFilesMap.entrySet())
            logPropertiesFileFor(diagnosticLog, propertiesMap.getKey(), propertiesMap.getValue());

        return new DiagnosticsResult(true, diagnosticLog.toString());
    }

    private void logPropertiesFileFor(DiagnosticLog diagnosticLog, String file, Properties properties) {
        diagnosticLog.add(file + ":\n");
        TreeSet sortedKeys = new TreeSet(properties.keySet());
        for (Object key : sortedKeys)
            diagnosticLog.add(key + "=" + properties.get(key));
        diagnosticLog.add("______________________________________________________________");
    }
}
