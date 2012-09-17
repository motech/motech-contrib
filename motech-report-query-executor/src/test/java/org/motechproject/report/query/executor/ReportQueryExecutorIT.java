package org.motechproject.report.query.executor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:testapplicationContext.xml")
public class ReportQueryExecutorIT {

    @Autowired
    private ReportQueryExecutor executorReport;

    @Test
    @Transactional
    public void shouldExecuteQueryAndReturnJsonResultSet(){
        String sql = "select * from pg_tables where schemaname=:schemaname";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("schemaname", "pg_catalog");
        String resultSet = executorReport.execute(sql, parameters);
        assertNotNull(resultSet);
    }
}
