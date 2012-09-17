package org.motechproject.report.query.executor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testapplicationContext.xml")
public class ReportQueryExecutorIT {

    @Autowired
    private ReportQueryExecutor executorReport;

    @Test
    @Transactional
    public void shouldExecuteQueryAndReturnJsonResultSet(){
        int pageSize = 10;
        int pageNumber = 1;
        String sql = "select tablename from pg_tables where schemaname=:schemaname and tablename=:tablename";

        String expected = "[{\"tablename\":\"pg_type\"}]";
        String expectedPaginatedResult = String.format("{pagenumber:%s, pagesize:%s, lastpage:true, firstpage:true, totalrows:%s, rows:%s}", pageNumber,  pageSize,  -1, expected);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("schemaname", "pg_catalog");
        parameters.put("tablename", "pg_type");

        String resultSet = executorReport.fetchJSONResultset(sql, parameters, new PageRequest(pageSize, pageNumber, false));

        assertEquals(expectedPaginatedResult, resultSet);
    }

    @Test
    @Transactional
    public void shouldExecuteQueryAndReturnEmptyJsonResultSet(){
        int pageSize = 10;
        int pageNumber = 1;
        String sql = "select * from pg_tables where schemaname=:schemaname and tablename=:tablename";

        String expected = "[]";
        String expectedPaginatedResult = String.format("{pagenumber:%s, pagesize:%s, lastpage:true, firstpage:true, totalrows:%s, rows:%s}", pageNumber,  pageSize,  -1, expected);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("schemaname", "pg_catalog");
        parameters.put("tablename", "does_not_exist");

        String resultSet = executorReport.fetchJSONResultset(sql, parameters, new PageRequest(pageSize, pageNumber, false));

        assertEquals(expectedPaginatedResult, resultSet);
    }

    @Test
    @Transactional
    public void shouldExecuteQueryAndFetchCount() {
        int pageSize = 1;
        int pageNumber = 1;
        String sql = "select tablename from pg_tables where schemaname=:schemaname";

        String expected = "[{\"tablename\":\"pg_statistic\"}]";
        String expectedPaginatedResult = String.format("{pagenumber:%s, pagesize:%s, lastpage:false, firstpage:true, totalrows:%s, rows:%s}", pageNumber,  pageSize,  "50", expected);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("schemaname", "pg_catalog");

        String resultSet = executorReport.fetchJSONResultset(sql, parameters, new PageRequest(pageSize, pageNumber, true));

        assertEquals(expectedPaginatedResult, resultSet);

    }
}
