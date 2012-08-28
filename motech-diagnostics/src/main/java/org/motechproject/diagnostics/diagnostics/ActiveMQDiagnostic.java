package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.controller.DiagnosticServiceName;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class ActiveMQDiagnostic implements Diagnostics {

    @Autowired(required = false)
    private CachingConnectionFactory connectionFactory;

    @Diagnostic(name = "Port active")
    public DiagnosticsResult<String> performDiagnosis() throws JMSException {

        Boolean isSuccess = checkActiveMQConnection();
        return new DiagnosticsResult<String>("Is Active", isSuccess.toString());
    }

    private boolean checkActiveMQConnection() {
        try {
            connectionFactory.getTargetConnectionFactory().createConnection().start();
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public String name() {
        return DiagnosticServiceName.ACTIVE_MQ;
    }

    @Override
    public boolean canPerformDiagnostics() {
        return connectionFactory != null;
    }
}
