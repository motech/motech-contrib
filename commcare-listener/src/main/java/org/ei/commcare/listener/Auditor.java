package org.ei.commcare.listener;

public interface Auditor {
    void auditFormSubmission(String formId, String formType, String formData);
}
