package org.ei.commcare.listener;

import org.springframework.stereotype.Component;

@Component
public class AuditorRegistrar {
    public Auditor auditor;

    void auditFormSubmission(String formId, String formType, String formData) {
        if (auditor == null) {
            return;
        }
        auditor.auditFormSubmission(formId, formType, formData);
    }

    void register(Auditor auditor) {
        this.auditor = auditor;
    }
}

