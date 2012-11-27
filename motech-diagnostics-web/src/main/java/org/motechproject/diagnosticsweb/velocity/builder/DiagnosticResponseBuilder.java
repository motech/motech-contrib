package org.motechproject.diagnosticsweb.velocity.builder;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.motechproject.diagnostics.configuration.DiagnosticConfiguration;
import org.motechproject.diagnostics.response.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.List;

@Component
public class DiagnosticResponseBuilder {

    private VelocityEngine velocityEngine;
    private DiagnosticConfiguration diagnosticConfiguration;

    @Autowired
    public DiagnosticResponseBuilder(VelocityEngine velocityEngine, DiagnosticConfiguration diagnosticConfiguration) {
        this.velocityEngine = velocityEngine;
        this.diagnosticConfiguration = diagnosticConfiguration;
    }

    public String createResponseMessage(List<ServiceResult> serviceResults) {
        Template template = velocityEngine.getTemplate("diagnostics-web/views/content/diagnosticResponse.vm");
        VelocityContext context = new VelocityContext();
        context.put("contextPath", diagnosticConfiguration.contextPath());
        context.put("links", diagnosticConfiguration.getLinks());
        context.put("serviceResults", serviceResults);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
}
