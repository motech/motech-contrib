package org.motechproject.diagnostics.diagnostics;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
@DirtiesContext
public class DBCPDataSourceDiagnosticsIT {

    @Autowired
    DBCPDataSourceDiagnostics dbcpDataSourceDiagnostics;

    @Test
    public void shouldAlwaysBeAbleToPerformDiagnostics() {
        assertTrue(dbcpDataSourceDiagnostics.canPerformDiagnostics());
    }


    @Test
    public void shouldReturnDBCPStatistics() {
       DiagnosticsResult result  = dbcpDataSourceDiagnostics.diagnostics();
       DiagnosticsResult diagnosticsResult = result.getResults().get(0);

       assertThat(diagnosticsResult.getResults().size(), greaterThan(0));
    }
}
