package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class ActiveMQDiagnostic {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Diagnostic(name = "ACTIVEMQ")
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
}
