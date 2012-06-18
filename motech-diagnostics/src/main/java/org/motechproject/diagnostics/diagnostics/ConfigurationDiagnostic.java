package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

@Component
public class ConfigurationDiagnostic {

    private HashMap<String, Properties> propertyFilesMap;

    public ConfigurationDiagnostic() {
    }

    @Autowired(required = false)
    public ConfigurationDiagnostic(HashMap<String, Properties> propertyFilesMap) {
        this.propertyFilesMap = propertyFilesMap;
    }

    @Diagnostic(name = "CONFIGURATION PROPERTIES")
    public DiagnosticsResult performDiagnosis() {
        if (propertyFilesMap == null) return null;
        DiagnosticLog diagnosticLog = new DiagnosticLog();

        for (Map.Entry<String, Properties> propertiesMap : propertyFilesMap.entrySet())
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
