package org.motechproject.report.query.executor;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
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
    public void shouldSetParametersAndFetchJsonResultSet() {
        String sql = "somesql";
        int pageSize = 21;
        int pageNumber = 10;

        List<String> collectionParameter = new ArrayList();
        String[] arrayParameter = new String[] {};
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("parameter1", "value1");
        parameters.put("parameter2", collectionParameter);
        parameters.put("parameter3", arrayParameter);

        Session session = mock(Session.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);

        SQLQuery selectQuery = mock(SQLQuery.class);
        when(session.createSQLQuery(wrapToSelectQuery(sql, pageSize, pageNumber))).thenReturn(selectQuery);
        when(selectQuery.list()).thenReturn(Arrays.asList("result1", "result2"));

        SQLQuery countQuery = mock(SQLQuery.class);
        when(session.createSQLQuery(wrapToCountQuery(sql))).thenReturn(countQuery);
        when(countQuery.list()).thenReturn(Arrays.asList(new BigInteger("42")));


        String resultSet = executorReport.fetchJSONResultset(sql, parameters, new PageRequest(pageSize, pageNumber, true));

        verify(selectQuery).setParameter("parameter1", "value1");
        verify(selectQuery).setParameterList("parameter2", collectionParameter);
        verify(selectQuery).setParameterList("parameter3", arrayParameter);

        assertEquals("{pagenumber:10, pagesize:21, lastpage:true, firstpage:false, totalrows:42, rows:[result1, result2]}", resultSet);
    }

    @Test
    public void shouldFetchJsonResultSetForDefaultPageRequest() {
        String sql = "somesql";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("parameter1", "value1");
        parameters.put("parameter2", "value2");

        Session session = mock(Session.class);
        SQLQuery selectQuery = mock(SQLQuery.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createSQLQuery(wrapToSelectQuery(sql, 20, 1))).thenReturn(selectQuery);
        when(selectQuery.list()).thenReturn(Arrays.asList("result1", "result2"));

        String resultSet = executorReport.fetchJSONResultset(sql, parameters);

        verify(selectQuery).setParameter("parameter1", "value1");
        verify(selectQuery).setParameter("parameter2", "value2");
        assertEquals("{pagenumber:1, pagesize:20, lastpage:true, firstpage:true, totalrows:-1, rows:[result1, result2]}", resultSet);
    }


    @Test
    public void shouldFetchJsonForEmptyResultSet() {
        String sql = "somesql";
        int pageSize = 21;
        int pageNumber = 1;

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("parameter1", "value1");
        parameters.put("parameter2", "value2");

        Session session = mock(Session.class);
        SQLQuery selectQuery = mock(SQLQuery.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.createSQLQuery(wrapToSelectQuery(sql, pageSize, pageNumber))).thenReturn(selectQuery);
        when(selectQuery.list()).thenReturn(new ArrayList());

        String resultSet = executorReport.fetchJSONResultset(sql, parameters, new PageRequest(pageSize, pageNumber, false));

        verify(selectQuery).setParameter("parameter1", "value1");
        verify(selectQuery).setParameter("parameter2", "value2");
        assertEquals("{pagenumber:1, pagesize:21, lastpage:true, firstpage:true, totalrows:-1, rows:[]}", resultSet);
    }

    @Test
    public void shouldGetTotalResultSetSizeWhenAskedFor() {
        String sql = "somesql";
        int pageSize = 21;
        int pageNumber = 1;
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("parameter1", "value1");
        parameters.put("parameter2", "value2");

        Session session = mock(Session.class);
        when(sessionFactory.getCurrentSession()).thenReturn(session);

        SQLQuery selectQuery = mock(SQLQuery.class);
        when(session.createSQLQuery(wrapToSelectQuery(sql, pageSize, pageNumber))).thenReturn(selectQuery);
        when(selectQuery.list()).thenReturn(new ArrayList());

        String resultSet = executorReport.fetchJSONResultset(sql, parameters, new PageRequest(pageSize, pageNumber, false));

        verify(selectQuery).setParameter("parameter1", "value1");
        verify(selectQuery).setParameter("parameter2", "value2");
        assertEquals("{pagenumber:1, pagesize:21, lastpage:true, firstpage:true, totalrows:-1, rows:[]}", resultSet);
    }

    private String wrapToSelectQuery(String sql, int pageSize, int pageNumber) {
        sql = String.format("%s limit %s offset %s", sql, pageSize + 1, pageSize*(pageNumber-1));
        return String.format("select text(row_to_json(t1)) from (%s) t1", sql);
    }

    private String wrapToCountQuery(String sql) {
        return String.format("select count(*) from (%s) t1", sql);
    }
}
