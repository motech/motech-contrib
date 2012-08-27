package org.motechproject.diagnostics;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.diagnostics.response.DiagnosticsResponse;

import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiagnosticResponseBuilderTest {

    @Mock
    private VelocityEngine velocityEngine;
    @Mock
    private Template template;
    private DiagnosticResponseBuilder builder;

    @Before
    public void setUp() {
        initMocks(this);
        builder = new DiagnosticResponseBuilder(velocityEngine);
    }

    @Test
    public void shouldSortResponsesBasedOnDiagnosticsName() {
        DiagnosticsResponse diagnosticsResponse1 = new DiagnosticsResponse("b", null);
        DiagnosticsResponse diagnosticsResponse2 = new DiagnosticsResponse("z", null);
        DiagnosticsResponse diagnosticsResponse3 = new DiagnosticsResponse("a", null);
        DiagnosticsResponse diagnosticsResponse4 = new DiagnosticsResponse("b", null);

        when(velocityEngine.getTemplate("/diagnosticResponse.vm")).thenReturn(template);

        builder.createResponseMessage(Arrays.asList(diagnosticsResponse1, diagnosticsResponse2, diagnosticsResponse3, diagnosticsResponse4));

        ArgumentCaptor<VelocityContext> contextArgumentCaptor = ArgumentCaptor.forClass(VelocityContext.class);
        verify(template).merge(contextArgumentCaptor.capture(), any(Writer.class));

        VelocityContext velocityContext = contextArgumentCaptor.getValue();
        List<DiagnosticsResponse> actualDiagnosticsResponses = (List<DiagnosticsResponse>) velocityContext.get("diagnosticsResponses");
        assertEquals("a", actualDiagnosticsResponses.get(0).getName());
        assertEquals("b", actualDiagnosticsResponses.get(1).getName());
        assertEquals("b", actualDiagnosticsResponses.get(2).getName());
        assertEquals("z", actualDiagnosticsResponses.get(3).getName());
    }
}
