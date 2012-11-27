package org.motechproject.diagnostics.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DiagnosticConfiguration {

    private Properties properties;

    @Autowired
    public DiagnosticConfiguration(Properties diagnosticProperties) {
        this.properties = diagnosticProperties;
    }

    public String activeMqQueueNames() {
        return properties.getProperty("activeMQ.queueNames");
    }


    public String diagnosticServices(){
        return properties.getProperty("diagnosticServices");
    }

    public String contextPath(){
        return properties.getProperty("application.context.path");
    }

    public String logFileLocation(){
        return properties.getProperty("log.location");
    }


    public List<Link> getLinks() {
        Map<String, Link> linkMap = new TreeMap<>();
        for(String key : properties.stringPropertyNames()){
            if(isLinkProperty(key)){
                createLink(key, properties.getProperty(key), linkMap);
            }
        }
        return new ArrayList<>(linkMap.values());
    }

    private boolean isLinkProperty(String key) {
        return key.startsWith("link.");
    }

    private void createLink(String key, String url, Map<String, Link> linkMap) {
        String menuName = key.substring(key.indexOf(".") + 1, key.lastIndexOf("."));
        String linkName = key.substring(key.lastIndexOf(".") + 1);

        if(!linkMap.containsKey(menuName)){
            linkMap.put(menuName, new Link(menuName));
        }

        linkMap.get(menuName).add(new Link(linkName, url));
    }
}


