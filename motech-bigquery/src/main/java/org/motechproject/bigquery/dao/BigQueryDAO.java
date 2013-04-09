package org.motechproject.bigquery.dao;


import org.motechproject.bigquery.model.FilterParams;
import org.motechproject.bigquery.response.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BigQueryDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private SqlQueryParamBuilder sqlQueryParamBuilder;

    BigQueryDAO() {
        //for spring proxy
    }

    @Autowired
    public BigQueryDAO(NamedParameterJdbcTemplate bigQueryJdbcTemplate, SqlQueryParamBuilder sqlQueryParamBuilder) {
        this.sqlQueryParamBuilder = sqlQueryParamBuilder;
        this.jdbcTemplate = bigQueryJdbcTemplate;
    }

    public QueryResult executeQuery(String query, FilterParams filterParams){
        Map<String, Object> queryParams = sqlQueryParamBuilder.buildQueryParams(filterParams);
        List<Map<String,Object>> list = jdbcTemplate.queryForList(query, queryParams);
        return new QueryResult(list);
    }
}
