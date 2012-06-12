package org.motechproject.diagnostics.diagnostics;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import java.util.Enumeration;

@Component
public class ActiveMQDiagnostic {

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private ActiveMQQueue eventQueue;

    @Diagnostic(name = "activeMq")
    public DiagnosticsResult performDiagnosis() throws JMSException {
        Connection connection = null;
        QueueBrowser browser = null;

        DiagnosticLog diagnosticLog = new DiagnosticLog("ACTIVEMQ");
        diagnosticLog.add("Checking for Active MQ connection");

        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = (ActiveMQConnectionFactory) connectionFactory.getTargetConnectionFactory();
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            diagnosticLog.add("Successfully opened connection");

            diagnosticLog.add("Opening browser for queue size");
            int queueSize = 0;
            browser = connection.createSession(false, Session.AUTO_ACKNOWLEDGE).createBrowser(eventQueue);
            Enumeration messages = browser.getEnumeration();
            while (messages.hasMoreElements()) {
            messages.nextElement();
            queueSize++;
        }
            diagnosticLog.add("Queue size is : " + queueSize);
        } catch (Exception e) {
            diagnosticLog.add("Error connecting to ActiveMQ");
            diagnosticLog.addError(e);
            return new DiagnosticsResult(false, diagnosticLog.toString());
        } finally {
            if (browser != null) browser.close();
            if (connection != null) connection.close();
        }
        return new DiagnosticsResult(true, diagnosticLog.toString());
    }
}