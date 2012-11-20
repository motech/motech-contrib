package org.motechproject.caselogs.velocity.builder;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.motechproject.caselogs.configuration.CaseLogConfiguration;
import org.motechproject.casexml.domain.CaseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.List;

@Component
public class CaseLogsResponseBuilder {

    public static final String VIEW_PATH = "case-logger-web/views/content/logs.vm";
    private VelocityEngine velocityEngine;
    private CaseLogConfiguration caseLogConfiguration;

    @Autowired
    public CaseLogsResponseBuilder(VelocityEngine velocityEngine, CaseLogConfiguration caseLogConfiguration) {
        this.velocityEngine = velocityEngine;
        this.caseLogConfiguration = caseLogConfiguration;
    }

    public String createResponseMessage(List<CaseLog> caseLogs) {
        Template template = velocityEngine.getTemplate(VIEW_PATH);
        VelocityContext context = new VelocityContext();
        context.put("contextPath", caseLogConfiguration.contextPath());
        context.put("caseLogs", caseLogs);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
