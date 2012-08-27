package org.motechproject.diagnostics.diagnostics;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.junit.Test;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.testing.utils.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(locations = "classpath*:/applicationContext-DiagnosticsTest.xml")
public class CouchDBDiagnosticsTest extends SpringIntegrationTest {


    @Autowired
    @Qualifier("couchDbInstance")
    private CouchDbInstance instance;

    @Autowired
    private List<CouchDbInstance> allInstances;

    @Autowired
    private CouchDBDiagnostics diagnostics;

    @Test
    public void eachInstanceHasResult() {
        List<DiagnosticsResult> results = diagnostics.isActive().getValue();
        assertEquals(allInstances.size(), results.size());
    }

    @Test
    public void shouldReturnPositiveResultWhenAbleToConnectToInstance() {
        List<DiagnosticsResult> results = diagnostics.isActive().getValue();
        assertEquals("true", results.get(0).getValue().toString());
    }

    @Test
    public void shouldReturnNegativeReturnWhenNotAbleToConnectToInstance() {
        CouchDbInstance instance = mock(CouchDbInstance.class);
        when(instance.getConnection()).thenReturn(null);

        List<DiagnosticsResult> results = diagnostics.isActive().getValue();
        assertEquals("error", results.get(1).getValue().toString());
    }

    @Override
    public CouchDbConnector getDBConnector() {
        return null;
    }
}
