package org.motechproject.report.query.executor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testApplicationContext.xml")
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
        String expectedPaginatedResult = String.format("{pagenumber:%s, pagesize:%s, lastpage:true, firstpage:true, totalrows:%s, rows:%s}", pageNumber,  pageSize,  0, expected);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("schemaname", "pg_catalog");
        parameters.put("tablename", "does_not_exist");

        String resultSet = executorReport.fetchJSONResultset(sql, parameters, new PageRequest(pageSize, pageNumber, true));

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

    @Test
    @Transactional
    public void shouldResultNestedJSON() {
        int pageSize = 1;
        int pageNumber = 1;
        String sql = "select t.schemaname, array_to_json(array_agg(row_to_json(stat))) as children from pg_tables t join (select schemaname, relname from pg_stat_all_tables) stat on stat.relname = t.tablename where stat.relname in (:relnames) group by t.schemaname order by t.schemaname";

        String expected = "[{\"schemaname\":\"information_schema\",\"children\":[{\"schemaname\":\"information_schema\",\"relname\":\"sql_languages\"},{\"schemaname\":\"information_schema\",\"relname\":\"sql_implementation_info\"}]}]";
        String expectedPaginatedResult = String.format("{pagenumber:%s, pagesize:%s, lastpage:true, firstpage:true, totalrows:%s, rows:%s}", pageNumber,  pageSize,  1, expected);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("relnames", Arrays.asList("sql_languages", "sql_implementation_info"));

        String resultSet = executorReport.fetchJSONResultset(sql, parameters, new PageRequest(pageSize, pageNumber, true));

        assertEquals(expectedPaginatedResult, resultSet);
    }
}
