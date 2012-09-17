package org.motechproject.report.query.executor;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class ReportQueryExecutor {

    private SessionFactory sessionFactory;

    @Autowired
    public ReportQueryExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public String execute(String sql, HashMap<String, Object> parameters) {
        Session currentSession = sessionFactory.getCurrentSession();
        String formattedSql = wrapSqlQueryWithJsonFunction(sql);
        SQLQuery sqlQuery = currentSession.createSQLQuery(formattedSql);

        for (String parameter : parameters.keySet()) {
            sqlQuery.setParameter(parameter, parameters.get(parameter));
        }
        List list = sqlQuery.list();
        return list.toString();
    }

    private String wrapSqlQueryWithJsonFunction(String sql) {
        return String.format("select row_to_json(t1) from (%s) t1", sql);
    }
}
