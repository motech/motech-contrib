package org.motechproject.diagnostics.diagnostics;

import org.apache.commons.lang.StringUtils;
import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.configuration.DiagnosticConfiguration;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.management.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.motechproject.diagnostics.response.Status.Fail;
import static org.motechproject.diagnostics.response.Status.Success;
import static org.motechproject.diagnostics.response.Status.Warn;

@Component
public class ActiveMQDiagnostic implements Diagnostics {

    @Autowired(required = false)
    private CachingConnectionFactory connectionFactory;

    @Autowired(required = false)
    private MBeanServerConnectionFactoryBean activeMQDiagnosticsClientConnector;

    @Autowired
    private DiagnosticConfiguration diagnosticConfiguration;

    @Diagnostic(name = "Port active")
    public DiagnosticsResult performDiagnosis() throws JMSException {

        Boolean isSuccess = checkActiveMQConnection();
        return new DiagnosticsResult("Active MQ Port Is Active", isSuccess.toString(), Success);
    }

    @Diagnostic(name = "Queue Size")
    public DiagnosticsResult queueSizes() throws JMSException, MalformedURLException {
        List<String> queueNameList = queueNamesList(diagnosticConfiguration.activeMqQueueNames());

        if (!queueNameList.isEmpty() && activeMQDiagnosticsClientConnector == null) {
            return new DiagnosticsResult("ActiveMQ Queue Sizes", "activeMQDiagnosticsClientConnector bean not defined", Warn);
        }

        if (queueNameList.isEmpty()) {
            return new DiagnosticsResult("ActiveMQ Queue Sizes", "No activeMQ.queueNames specified.", Warn);
        }

        List<DiagnosticsResult> results = new ArrayList<>();
        for (String queueName : queueNameList) {
            try {
                ObjectName objectNameRequest = new ObjectName("org.apache.activemq:BrokerName=localhost,Type=Queue,Destination=" + queueName);
                Long queueSize = (Long) activeMQDiagnosticsClientConnector.getObject().getAttribute(objectNameRequest, "QueueSize");
                if (queueSize == null) {
                    results.add(new DiagnosticsResult("Queue Size for " + queueName, "Not Found!", Fail));
                } else {
                    results.add(new DiagnosticsResult("Queue Size for " + queueName, queueSize.toString(), Success));
                }
            } catch (Exception e) {
                return new DiagnosticsResult(queueName, "Error occurred while connecting", Fail);
            }
        }
        return new DiagnosticsResult("ActiveMQ Queue Sizes", results);
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


    public List<String> queueNamesList(String queueNames) {
        if (StringUtils.isNotEmpty(queueNames))
            return Arrays.asList(queueNames.split(","));
        else
            return new ArrayList<>();
    }
}
