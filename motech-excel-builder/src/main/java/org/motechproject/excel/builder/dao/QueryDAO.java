package org.motechproject.excel.builder.dao;

import org.motechproject.excel.builder.config.ReportQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class QueryDAO {

    private JdbcTemplate jdbcTemplate;

    private ReportQueries reportQueries;

    @Autowired
    public QueryDAO(JdbcTemplate jdbcTemplate, ReportQueries reportQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.reportQueries = reportQueries;
    }

    public List<Map<String, Object>> getData(String reportType) {
        String sqlQuery = reportQueries.getQuery(reportType);
        return jdbcTemplate.queryForList(sqlQuery);
    }
}
