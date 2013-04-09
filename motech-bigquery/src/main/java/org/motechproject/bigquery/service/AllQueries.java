package org.motechproject.bigquery.service;

import java.util.Map;


public class AllQueries {
    Map<String, String> queries;

    public AllQueries(Map<String, String> queries) {
        this.queries = queries;
    }

    public String getQuery(String queryName){
        return queries.get(queryName);
    }
}
