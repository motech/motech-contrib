package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.motechproject.diagnostics.response.Status.Success;

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
    public DiagnosticsResult performDiagnosis() {
        List<DiagnosticsResult> results = new ArrayList<>();
        results.add(new DiagnosticsResult("Is Active", "true", Success));
        results.addAll(propertiesInAllFiles());
        return new DiagnosticsResult("CONFIGURATION PROPERTIES", results);
    }

    private List<DiagnosticsResult> propertiesInAllFiles()
    {
        List<DiagnosticsResult> properties = new ArrayList<>();
        for (Map.Entry<String, Properties> entry : propertyFilesMap.entrySet()) {
            properties.add(new DiagnosticsResult(entry.getKey(), propertiesInFile(entry.getValue())));
        }
        return properties;
    }
    private List<DiagnosticsResult> propertiesInFile(Properties propertiesFile) {
        List<DiagnosticsResult> properties = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : propertiesFile.entrySet()) {
            properties.add(new DiagnosticsResult(entry.getKey().toString(), entry.getValue().toString(), Success));
        }
        return properties;
    }

    @Override
    public String name() {
        return DiagnosticServiceName.CONFIGURATION;
    }

    @Override
    public boolean canPerformDiagnostics() {
        return propertyFilesMap != null;
    }
}
