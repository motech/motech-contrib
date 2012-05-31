package org.ei.commcare.listener;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuditorRegistrarTest {
    @Test
    public void shouldRegisterAnAuditorToReceiveAuditMessages() {
        AuditorRegistrar registrar = new AuditorRegistrar();
        Auditor auditor = mock(Auditor.class);

        registrar.register(auditor);
        registrar.auditFormSubmission("X", "Y", "Z");

        verify(auditor).auditFormSubmission("X", "Y", "Z");
    }

    @Test
    public void shouldNotFailIfNoAuditorHasBeenRegistered() {
        AuditorRegistrar registrar = new AuditorRegistrar();
        registrar.auditFormSubmission("X", "Y", "Z");
    }
}
