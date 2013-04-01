package org.motechproject.excel.builder.service;


import org.motechproject.excel.builder.dao.QueryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryService {

    private QueryDAO queryDAO;

    @Autowired
    public QueryService(QueryDAO queryDAO) {
        this.queryDAO = queryDAO;
    }

    public List<Map<String, Object>> getData(String reportType) {
        return queryDAO.getData(reportType);
    }
}
