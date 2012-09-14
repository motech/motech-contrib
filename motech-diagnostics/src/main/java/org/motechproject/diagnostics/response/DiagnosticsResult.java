package org.motechproject.diagnostics.response;

public class DiagnosticsResult<T> {

    private String name;
    private T value;

    public DiagnosticsResult(String name, T value) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
