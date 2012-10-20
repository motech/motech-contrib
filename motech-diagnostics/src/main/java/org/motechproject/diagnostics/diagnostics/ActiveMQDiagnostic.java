package org.motechproject.diagnostics.diagnostics;

import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.controller.DiagnosticServiceName;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.management.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ActiveMQDiagnostic implements Diagnostics {

    @Autowired(required = false)
    private CachingConnectionFactory connectionFactory;

    @Autowired(required = false)
    private MBeanServerConnectionFactoryBean activeMQClientConnector;

    private List<String> queueNameList;

    @Diagnostic(name = "Port active")
    public DiagnosticsResult<String> performDiagnosis() throws JMSException {

        Boolean isSuccess = checkActiveMQConnection();
        return new DiagnosticsResult<String>("Active MQ Port Is Active", isSuccess.toString());
    }

    @Diagnostic(name = "Queue Size")
    public DiagnosticsResult<String> queueSizes() throws JMSException {
        List<DiagnosticsResult> results = new ArrayList<>();
        for(String queueName : queueNameList) {
            try {
                ObjectName objectNameRequest = new ObjectName(
                        "org.apache.activemq:BrokerName=localhost,Type=Queue,Destination=" + queueName);
                Long queueSize = (Long) activeMQClientConnector.getObject().getAttribute(objectNameRequest, "QueueSize");
                results.add(new DiagnosticsResult<String>("Queue Size for " + queueName, queueSize.toString()));
            } catch (Exception e) {
                return new DiagnosticsResult("Connecting to queue: " + queueName , "Error");
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

    @Value("${activeMQ.queueNames}")
    public void queueNames(String queueNames){
        queueNameList = Arrays.asList(queueNames.split(","));

    }
}
