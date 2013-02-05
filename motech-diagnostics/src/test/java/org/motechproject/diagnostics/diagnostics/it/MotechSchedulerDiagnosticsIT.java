package org.motechproject.diagnostics.diagnostics.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.diagnostics.MotechSchedulerDiagnostics;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.Status;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
@DirtiesContext
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

    @Test
    public void shouldGetQuartzJMXDiagnostics() throws SchedulerException {
        DiagnosticsResult diagnosticsResult = motechSchedulerDiagnostics.quartzMonitoring();
        assertEquals("Quartz Monitoring", diagnosticsResult.getName());
        assertEquals(Status.Success, diagnosticsResult.getStatus());
        assertThat(diagnosticsResult.getResults().size(), greaterThan(0));
    }
}