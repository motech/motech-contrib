package org.motechproject.paginator.contract;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

public class FilterParams extends HashMap<String, Object> {

    public FilterParams removeEmptyParams() {
        FilterParams properties = new FilterParams();
        for (Object key : this.keySet()) {
            Object value = this.get(key);
            if (!StringUtils.isBlank((String) value)) {
                properties.put(key.toString(), value.toString());
            }
        }
        return properties;
    }

}
