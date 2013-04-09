package org.motechproject.bigquery.dao;

import org.motechproject.bigquery.model.FilterParams;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class SqlQueryParamBuilder {

    public static final String DATE_FORMAT = "dd/MM/yyyy";

    private SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    public static final String DATE_SUFFIX = "_date";
    public static final String INT_SUFFIX = "_int";

    public Map<String, Object> buildQueryParams(FilterParams filterParams) {
        Map<String, Object> queryParams = new HashMap<>();
        for (Map.Entry<String, Object> entry : filterParams.entrySet()) {
            String key = entry.getKey();
            String stringValue = (String) entry.getValue();
            queryParams.put(key, convertToType(key, stringValue));
        }
        return queryParams;
    }

    private Object convertToType(String key, String stringValue) {
        if (key.endsWith(DATE_SUFFIX)) {
            return getDateValue(stringValue);
        }

        if (key.endsWith(INT_SUFFIX)) {
            return Integer.parseInt(stringValue);
        }

        return stringValue;
    }

    private Object getDateValue(String stringValue) {
        try {
            return formatter.parse(stringValue);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
