package org.motechproject.report.query.executor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PageRequest {
    private int pageSize;
    private int pageNumber;
    private boolean fetchAllRecordsCount;

    public static final PageRequest DEFAULT = new PageRequest(20, 1, false);
    private boolean totalRowsCount;

    public PageRequest(int pageSize, int pageNumber, boolean fetchAllRecordsCount) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.fetchAllRecordsCount = fetchAllRecordsCount;
    }

    public String paginateSql(String sql) {
        int offset = pageSize*(pageNumber-1);
        int limit = pageSize + 1;
        return String.format("%s limit %s offset %s",sql, limit, offset);
    }


    public String paginateResultSet(List resultSet, BigInteger allRecordsCount) {
        String paginatedResultSetFormat = "{pagenumber:%s, pagesize:%s, lastpage:%s, firstpage:%s, totalrows:%s, rows:%s}";

        boolean isLastPage = resultSet.size() <= pageSize;
        boolean isFirstPage = pageNumber == 1;

        ArrayList<String> resultSetCopy = new ArrayList<String>(resultSet);
        if(!isLastPage) {
            resultSetCopy.remove(pageSize);
        }

        return String.format(paginatedResultSetFormat, pageNumber,  pageSize, isLastPage, isFirstPage, allRecordsCount, resultSetCopy.toString());
    }

    public boolean fetchAllRecordsCount() {
        return fetchAllRecordsCount;
    }
}
