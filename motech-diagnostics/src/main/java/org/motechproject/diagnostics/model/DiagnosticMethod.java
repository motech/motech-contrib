package org.motechproject.diagnostics.model;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DiagnosticMethod {

    private String name;
    private Method method;
    private Diagnostics bean;

    public DiagnosticMethod(String name, Diagnostics diagnostics, Method method) {
        this.name = name;
        this.method = method;
        this.bean = diagnostics;
    }

    public static boolean isValidDiagnosticMethod(Method method) {
        return method.isAnnotationPresent(Diagnostic.class);
    }

    public DiagnosticsResult run() throws InvocationTargetException, IllegalAccessException {
        DiagnosticsResult result = (DiagnosticsResult) method.invoke(bean, null);
        return result == null ? null : result;
    }

    public String getName() {
        return name;
    }

    public String getDiagnosticServiceName() {
        return bean.name();
    }
}
