package org.motechproject.report.query.executor;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
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

        for (String parameter : parameters.keySet()) {
            selectQuery.setParameter(parameter, parameters.get(parameter));
        }

        BigInteger allRecordsCount = pageRequest.fetchAllRecordsCount() ? getAllRecordsCount(sql, parameters, currentSession) : BigInteger.valueOf(-1);

        return pageRequest.paginateResultSet(selectQuery.list(), allRecordsCount);
    }

    public BigInteger getAllRecordsCount(String sql, HashMap<String, Object> parameters, Session currentSession) {
        SQLQuery countQuery = currentSession.createSQLQuery(wrapSqlQueryForCount(sql));
        for (String parameter : parameters.keySet()) {
            countQuery.setParameter(parameter, parameters.get(parameter));
        }
        return (BigInteger)countQuery.list().get(0);
    }

    private String wrapSqlQueryWithJsonFunction(String sql) {
        return String.format("select text(row_to_json(t1)) from (%s) t1", sql);
    }

    private String wrapSqlQueryForCount(String sql) {
        return String.format("select count(*) from (%s) t1", sql);
    }

}
