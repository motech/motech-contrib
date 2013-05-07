package org.motechproject.bigquery.response;

import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode
public class QueryResult {
    List<Map<String,Object>> content;

    public QueryResult(List<Map<String, Object>> content) {
        this.content = content;
    }

    public List<Map<String, Object>> getContent() {
        return content;
    }
}
