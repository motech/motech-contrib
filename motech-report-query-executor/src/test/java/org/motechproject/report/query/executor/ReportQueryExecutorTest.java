package org.motechproject.report.query.executor;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportQueryExecutorTest {
    @Mock
    private SessionFactory sessionFactory;
    private ReportQueryExecutor executorReport;

    @Before
    public void setUp(){
        initMocks(this);
        executorReport = new ReportQueryExecutor(sessionFactory);
    }

    @Test
    public void shouldFetchJsonResultSet() {
        String sql = "somesql";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("parameter1", "value1");
        parameters.put("parameter2", "value2");

        Session session = mock(Session.class);
        SQLQuery sqlQuery = mock(SQLQuery.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createSQLQuery(wrapSqlToFetchJSON(sql))).thenReturn(sqlQuery);
        when(sqlQuery.list()).thenReturn(Arrays.asList("result1", "result2"));

        String resultSet = executorReport.execute(sql, parameters);

        verify(sqlQuery).setParameter("parameter1", "value1");
        verify(sqlQuery).setParameter("parameter2", "value2");
        assertEquals("[result1, result2]", resultSet);
    }

    private String wrapSqlToFetchJSON(String sql) {
        return String.format("select row_to_json(t1) from (%s) t1", sql);
    }
}
