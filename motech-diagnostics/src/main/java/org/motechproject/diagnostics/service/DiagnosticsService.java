package org.motechproject.diagnostics.service;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiagnosticsService {

    private Map<String, Diagnostics> diagnostics;

    @Autowired
    public DiagnosticsService(List<Diagnostics> diagnostics) {
        this.diagnostics = new HashMap<String, Diagnostics>();
        for (Diagnostics diagnostic : diagnostics) {
            if (diagnostic.canPerformDiagnostics()) {
                this.diagnostics.put(diagnostic.name(), diagnostic);
            }
        }
    }

    public List<DiagnosticsResult> run(String name) {
        Diagnostics diagnostic = diagnostics.get(name);
        AllDiagnosticMethods allDiagnosticMethods = new AllDiagnosticMethods(diagnostic);
        try {
            return allDiagnosticMethods.runAllDiagnosticMethods();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DiagnosticsResult> runAll() {
        List<DiagnosticsResult> results = new ArrayList<DiagnosticsResult>();
        for (String key : diagnostics.keySet()) {
            results.addAll(run(key));
        }
        return results;
    }
}
