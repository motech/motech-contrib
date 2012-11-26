package org.motechproject.diagnostics.diagnostics.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.diagnostics.MotechSchedulerDiagnostics;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class MotechSchedulerDiagnosticsIT {

    @Autowired
    private MotechSchedulerDiagnostics motechSchedulerDiagnostics;

    @Test
    public void shouldCheckIfSchedulerIsRunning() throws SchedulerException {
        assertEquals("Running", motechSchedulerDiagnostics.isSchedulerRunning().getValue());
    }

    @Test
    public void shouldCheckSchedulerJobStatuses() throws SchedulerException {
        DiagnosticsResult diagnosticsResult = motechSchedulerDiagnostics.jobStatuses();
        assertNotNull(diagnosticsResult.getResults());
    }
}