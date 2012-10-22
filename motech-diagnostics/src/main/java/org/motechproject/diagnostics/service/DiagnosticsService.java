package org.motechproject.diagnostics.service;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.configuration.DiagnosticConfiguration;
import org.motechproject.diagnostics.repository.AllDiagnosticMethods;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.Arrays.asList;

@Service
public class DiagnosticsService {

    private Map<String, Diagnostics> diagnostics;

    @Autowired
    public DiagnosticsService(List<Diagnostics> diagnostics, DiagnosticConfiguration diagnosticProperties) {
        List<String> configuredDiagnosticServices = asList(diagnosticProperties.diagnosticServices().split(","));
        this.diagnostics = new HashMap<>();
        for (Diagnostics diagnostic : diagnostics) {
            if (configuredDiagnosticServices.contains(diagnostic.name()) && diagnostic.canPerformDiagnostics()) {
                this.diagnostics.put(diagnostic.name(), diagnostic);
            }
        }
    }

    public ServiceResult run(String name) {
        Diagnostics diagnostic = diagnostics.get(name);
        AllDiagnosticMethods allDiagnosticMethods = new AllDiagnosticMethods(diagnostic);
        try {
            return new ServiceResult(name, allDiagnosticMethods.runAllDiagnosticMethods());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ServiceResult> runAll() {
        List<ServiceResult> results = new ArrayList<>();
        for (String key : diagnostics.keySet()) {
            results.add(run(key));
        }
        return results;
    }
}
