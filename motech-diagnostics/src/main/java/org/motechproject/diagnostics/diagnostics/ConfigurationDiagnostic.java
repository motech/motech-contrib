package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConfigurationDiagnostic implements Diagnostics {

    private HashMap<String, Properties> propertyFilesMap;

    public ConfigurationDiagnostic() {
    }

    @Autowired(required = false)
    public ConfigurationDiagnostic(HashMap<String, Properties> propertyFilesMap) {
        this.propertyFilesMap = propertyFilesMap;
    }

    @Diagnostic(name = "All properties")
    public DiagnosticsResult<List<DiagnosticsResult>> performDiagnosis() {
        if (propertyFilesMap == null) return null;
        List<DiagnosticsResult> results = new ArrayList<DiagnosticsResult>();
        results.add(new DiagnosticsResult("Is Active", "true"));
        results.addAll(propertiesInAllFiles());
        return new DiagnosticsResult<List<DiagnosticsResult>>("CONFIGURATION PROPERTIES", results);
    }

    private List<DiagnosticsResult> propertiesInAllFiles() {
        List<DiagnosticsResult> properties = new ArrayList<DiagnosticsResult>();
        for (Map.Entry<String, Properties> entry : propertyFilesMap.entrySet()) {
            properties.add(new DiagnosticsResult(entry.getKey(), propertiesInFile(entry.getValue())));
        }
        return properties;
    }

    private List<DiagnosticsResult<String>> propertiesInFile(Properties propertiesFile) {
        List<DiagnosticsResult<String>> properties = new ArrayList<DiagnosticsResult<String>>();
        for (Map.Entry<Object, Object> entry : propertiesFile.entrySet()) {
            properties.add(new DiagnosticsResult<String>(entry.getKey().toString(), entry.getValue().toString()));
        }
        return properties;
    }

    @Override
    public String name() {
        return "CONFIGURATION PROPERTIES";
    }
}
