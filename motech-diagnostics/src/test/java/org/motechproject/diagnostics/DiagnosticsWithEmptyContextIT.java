package org.motechproject.diagnostics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.ServiceResult;
import org.motechproject.diagnostics.service.DiagnosticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-TestEmptyDiagnostics.xml")
public class DiagnosticsWithEmptyContextIT {

    @Autowired
    private DiagnosticsService service;

    @Test
    public void shouldNotPerformAnyDiagnostics() {
        List<ServiceResult> results = service.runAll();
        assertTrue(results.isEmpty());
    }
}