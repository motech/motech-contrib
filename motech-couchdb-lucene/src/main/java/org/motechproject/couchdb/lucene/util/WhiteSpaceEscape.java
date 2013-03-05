package org.motechproject.couchdb.lucene.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WhiteSpaceEscape {

    public Map<String, Object> escape( Map<String, Object>  properties) {
        Map<String, Object> escapedProperties = new HashMap<>();
        for (Object o : properties.keySet()) {
            String result = properties.get(o).toString().replace(" ", "\\ ");
            escapedProperties.put(o.toString(), result);
        }
        return escapedProperties;
    }
}