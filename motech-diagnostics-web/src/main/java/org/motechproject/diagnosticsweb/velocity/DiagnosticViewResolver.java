package org.motechproject.diagnosticsweb.velocity;

import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

public class DiagnosticViewResolver extends VelocityLayoutViewResolver {
    @Override
    protected Class requiredViewClass() {
        return DiagnosticView.class;
    }

}
