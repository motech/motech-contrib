package org.motechproject.report.query.executor;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PageRequestTest {
    @Test
    public void shouldAddPaginationClausesForSQL() {
        PageRequest pageRequest = new PageRequest(20, 5, false);
        String sql = "select * from pg_tables";
        String expectedSql = sql + " limit 21 offset 80";

        String paginatedSql = pageRequest.paginateSql(sql);

        assertEquals(expectedSql,paginatedSql);
    }

    @Test
    public void shouldSetLastPageAsTrueIfEqualRecordsFetchedThanPageSize() {
        PageRequest pageRequest = new PageRequest(20, 5,false);
        String paginatedResultSet = pageRequest.paginateResultSet(Arrays.asList("value1", "value2"), BigInteger.valueOf(100));
        String expectedResultSet = "{pagenumber:5, pagesize:20, lastpage:true, firstpage:false, totalrows:100, rows:[value1, value2]}";
        assertEquals(expectedResultSet, paginatedResultSet);
    }

    @Test
    public void shouldSetLastPageAsTrueIfLessRecordsFetchedThanPageSize() {
        PageRequest pageRequest = new PageRequest(20, 5,false);
        String paginatedResultSet = pageRequest.paginateResultSet(Arrays.asList("value1"), BigInteger.valueOf(-1));
        String expectedResultSet = "{pagenumber:5, pagesize:20, lastpage:true, firstpage:false, totalrows:-1, rows:[value1]}";
        assertEquals(expectedResultSet, paginatedResultSet);
    }

    @Test
    public void shouldSetLastPageAsFalseIfMoreRecordsFetchedThanPageSize() {
        PageRequest pageRequest = new PageRequest(2, 4,false);
        String paginatedResultSet = pageRequest.paginateResultSet(Arrays.asList("value1", "value2", "value3"), BigInteger.valueOf(23));
        String expectedResultSet = "{pagenumber:4, pagesize:2, lastpage:false, firstpage:false, totalrows:23, rows:[value1, value2]}";
        assertEquals(expectedResultSet, paginatedResultSet);
    }

    @Test
    public void shouldSetFirstPageAsTrueIfPageNumberIsOne() {
        PageRequest pageRequest = new PageRequest(2, 1,false);
        String paginatedResultSet = pageRequest.paginateResultSet(Arrays.asList("value1", "value2", "value3"), BigInteger.valueOf(23));
        String expectedResultSet = "{pagenumber:1, pagesize:2, lastpage:false, firstpage:true, totalrows:23, rows:[value1, value2]}";
        assertEquals(expectedResultSet, paginatedResultSet);
    }
}
