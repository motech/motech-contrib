package org.motechproject.report.query.executor;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;

@Component
public class ReportQueryExecutor {

    private SessionFactory sessionFactory;

    @Autowired
    public ReportQueryExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public String fetchJSONResultset(String sql, HashMap<String, Object> parameters) {
        return fetchJSONResultset(sql, parameters, PageRequest.DEFAULT);
    }

    public String fetchJSONResultset(String sql, HashMap<String, Object> parameters, PageRequest pageRequest) {
        Session currentSession = sessionFactory.getCurrentSession();

        String formattedSql = wrapSqlQueryWithJsonFunction(pageRequest.paginateSql(sql));

        SQLQuery selectQuery = currentSession.createSQLQuery(formattedSql);

        setQueryParameters(selectQuery, parameters);

        System.out.println(selectQuery.getQueryString());

        BigInteger allRecordsCount = pageRequest.fetchAllRecordsCount() ? getAllRecordsCount(sql, parameters, currentSession) : BigInteger.valueOf(-1);

        return pageRequest.paginateResultSet(selectQuery.list(), allRecordsCount);
    }

    private void setQueryParameters(SQLQuery selectQuery, HashMap<String, Object> parameters) {
        for (String parameterName : parameters.keySet()) {
            Object parameterValue = parameters.get(parameterName);
            if(parameterValue instanceof Collection) {
                selectQuery.setParameterList(parameterName, (Collection) parameterValue);
                continue;
            }
            if(parameterValue instanceof Object[]) {
                selectQuery.setParameterList(parameterName, (Object[]) parameterValue);
                continue;
            }
            selectQuery.setParameter(parameterName, parameterValue);
        }
    }

    private BigInteger getAllRecordsCount(String sql, HashMap<String, Object> parameters, Session currentSession) {
        SQLQuery countQuery = currentSession.createSQLQuery(wrapSqlQueryForCount(sql));
        setQueryParameters(countQuery, parameters);
        return (BigInteger) countQuery.list().get(0);
    }

    private String wrapSqlQueryWithJsonFunction(String sql) {
        return String.format("select text(row_to_json(t1)) from (%s) t1", sql);
    }

    private String wrapSqlQueryForCount(String sql) {
        return String.format("select count(*) from (%s) t1", sql);
    }

}
