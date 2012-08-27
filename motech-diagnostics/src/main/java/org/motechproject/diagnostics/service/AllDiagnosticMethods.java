package org.motechproject.diagnostics.service;

import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.model.DiagnosticMethod;
import org.motechproject.diagnostics.response.DiagnosticsResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.motechproject.diagnostics.model.DiagnosticMethod.isValidDiagnosticMethod;

public class AllDiagnosticMethods {

    List<DiagnosticMethod> diagnosticMethods = new ArrayList<DiagnosticMethod>();

    public AllDiagnosticMethods(Object bean) {
        for (Method method : bean.getClass().getDeclaredMethods())
            if (isValidDiagnosticMethod(method))
                diagnosticMethods.add(new DiagnosticMethod(method.getAnnotation(Diagnostic.class).name(), bean, method));
    }

    public List<DiagnosticsResult> runAllDiagnosticMethods() throws InvocationTargetException, IllegalAccessException {
        List<DiagnosticsResult> diagnosticsResponses = new ArrayList<DiagnosticsResult>();
        for (DiagnosticMethod diagnosticMethod : diagnosticMethods) {
            DiagnosticsResult response = diagnosticMethod.run();
            if (response != null)
                diagnosticsResponses.add(response);
        }
        return diagnosticsResponses;
    }
}