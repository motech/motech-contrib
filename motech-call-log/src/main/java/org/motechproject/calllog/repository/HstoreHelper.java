package org.motechproject.calllog.repository;


import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HstoreHelper {

    private static final String KEY_VALUE_SEPARATOR = "=>";
    private static final String COMMA_SEPARATOR = ", ";

    public static String toString(Map<String, String> map) {
        if (map.isEmpty()) {
            return "";
        }

        List<String> combinedKeyValuePairs = new ArrayList<>();
        for(String key : map.keySet()) {
            combinedKeyValuePairs.add("\"" + key + "\"" + KEY_VALUE_SEPARATOR + "\"" + map.get(key) + "\"");
        }

        return StringUtils.join(combinedKeyValuePairs.toArray(), COMMA_SEPARATOR);
    }

    public static Map<String, String> toMap(String combinedString) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(combinedString)) {
            return map;
        }

        String[] keyValuePairs = combinedString.split(COMMA_SEPARATOR);
        for (String keyValuePair : keyValuePairs) {
            String[] keyValueArray = keyValuePair.split(KEY_VALUE_SEPARATOR);
            map.put(extractValue(keyValueArray[0]), extractValue(keyValueArray[1]));
        }
        return map;
    }

    private static String extractValue(String value) {
        return value.trim().substring(1, value.length() - 1);
    }
}