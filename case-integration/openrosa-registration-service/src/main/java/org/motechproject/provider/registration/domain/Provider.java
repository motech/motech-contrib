package org.motechproject.provider.registration.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/15/12
 * Time: 3:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Provider {
    public Provider(){
        fieldValues = new HashMap<String, String>();
    }
    
    public Map<String, String> getFieldValues() {
        return fieldValues;
    }

    public Map<String,String> fieldValues;

    public void AddFieldvalue(String nodeName, String textContent) {
        fieldValues.put(nodeName,textContent);
    }
}
