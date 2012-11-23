package org.motechproject.caselogs.velocity.builder;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.caselogs.configuration.CaseLogConfiguration;
import org.motechproject.casexml.domain.CaseLog;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CaseLogsResponseBuilderTest {

    CaseLogsResponseBuilder responseBuilder;

    @Mock
    private VelocityEngine velocityEngine;
    @Mock
    private CaseLogConfiguration caseLogConfiguration;

    @Before
    public void setUp() {
        initMocks(this);
        responseBuilder = new CaseLogsResponseBuilder(velocityEngine, caseLogConfiguration);
    }

    @Test
    public void shouldBuildResponseFromCaseLogs() {
        String viewPath = "case-log-browser/views/content/logs.vm";
        String contextPath = "context path";
        List<CaseLog> caseLogs = new ArrayList<>();
        CaseLog caseLog1 = new CaseLog("id1", "type1", "body", "url", false, DateTime.now());
        CaseLog caseLog2 = new CaseLog("id2", "type2", "another body", "another url", false, DateTime.now());
        caseLogs.add(caseLog1);
        caseLogs.add(caseLog2);

        Template template = mock(Template.class);
        when(velocityEngine.getTemplate(viewPath)).thenReturn(template);
        when(caseLogConfiguration.contextPath()).thenReturn(contextPath);

        responseBuilder.createResponseMessage(caseLogs);

        verify(velocityEngine).getTemplate(viewPath);
        verify(caseLogConfiguration).contextPath();
        verify(template).merge(any(Context.class), any(StringWriter.class));
    }
}
