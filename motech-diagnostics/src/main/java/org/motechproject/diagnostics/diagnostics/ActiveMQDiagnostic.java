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

    @Diagnostic(name = "activeMq")
    public DiagnosticsResult performDiagnosis() throws JMSException {
        DiagnosticLog diagnosticLog = new DiagnosticLog("ACTIVEMQ");
        boolean isSuccess = checkActiveMQConnection(diagnosticLog);
        return new DiagnosticsResult(isSuccess, diagnosticLog.toString());
    }

    private boolean checkActiveMQConnection(DiagnosticLog diagnosticLog) {
        diagnosticLog.add("Checking for Active MQ connection ...");
        try {
            connectionFactory.getTargetConnectionFactory().createConnection().start();
            diagnosticLog.add("Successfully opened connection.");
            return true;
        } catch (Exception ex) {
            diagnosticLog.add("Error connecting to ActiveMQ.");
            diagnosticLog.addError(ex);
        }
        return false;
    }
}
