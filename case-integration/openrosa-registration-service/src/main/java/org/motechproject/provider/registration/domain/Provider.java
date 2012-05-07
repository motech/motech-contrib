package org.motechproject.provider.registration.domain;

import java.util.HashMap;
import java.util.Map;

public class Provider {

    private String api_key;

    public Map<String, String> fieldValues;

    public Provider() {
        fieldValues = new HashMap<String, String>();
    }

    public Map<String, String> getFieldValues() {
        return fieldValues;
    }

    public void AddFieldvalue(String nodeName, String textContent) {
        fieldValues.put(nodeName, textContent);
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }
}
