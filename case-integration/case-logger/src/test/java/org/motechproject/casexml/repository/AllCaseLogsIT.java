package org.motechproject.casexml.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.casexml.domain.CaseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-caselogger.xml")
public class AllCaseLogsIT {

    private static final String ID_1 = "id1";
    private static final String ID_2 = "id2";
    private static final String ID_3 = "id3";
    private static final String ID_4 = "id4";
    private static final String TYPE_1 = "type1";
    private static final String TYPE_2 = "type2";
    public static final int NUMBER_OF_TEST_LOGS = 4;
    private CaseLog log1;
    private CaseLog log2;
    private CaseLog log3;
    private CaseLog log4;

    @Autowired
    private AllCaseLogs allCaseLogs;

    @Before
    public void setUp() {
        log1 = new CaseLog(ID_1, TYPE_1, null, null, false, null);
        log2 = new CaseLog(ID_2, TYPE_1, null, null, false, null);
        log3 = new CaseLog(ID_3, TYPE_2, null, null, false, null);
        log4 = new CaseLog(ID_4, TYPE_2, null, null, false, null);
        allCaseLogs.add(log1);
        allCaseLogs.add(log2);
        allCaseLogs.add(log3);
        allCaseLogs.add(log4);
    }

    @After
    public void tearDown() {
        allCaseLogs.remove(log1);
        allCaseLogs.remove(log2);
        allCaseLogs.remove(log3);
        allCaseLogs.remove(log4);
    }

    @Test
    public void shouldLimitLogsBasedOnConfiguredLimit() {
        List<CaseLog> caseLogs = allCaseLogs.getLatestLogs(NUMBER_OF_TEST_LOGS - 2);
        assertEquals(NUMBER_OF_TEST_LOGS - 2, caseLogs.size());
    }

    @Test
    public void shouldFilterCaseLogsByEntityId() {
        List<CaseLog> caseLogs = allCaseLogs.filterByEntityId("junk", NUMBER_OF_TEST_LOGS);
        assertTrue(caseLogs.isEmpty());

        caseLogs = allCaseLogs.filterByEntityId(ID_1, NUMBER_OF_TEST_LOGS);
        assertNotNull(caseLogs);
        assertEquals(1, caseLogs.size());
        assertEquals(ID_1, caseLogs.get(0).getEntityId());
    }

    @Test
    public void shouldFilterCaseLogsByRequestType() {
        List<CaseLog> caseLogs = allCaseLogs.filterByRequestType("junk", NUMBER_OF_TEST_LOGS);
        assertTrue(caseLogs.isEmpty());

        caseLogs = allCaseLogs.filterByRequestType(TYPE_1, NUMBER_OF_TEST_LOGS);
        assertNotNull(caseLogs);
        assertEquals(2, caseLogs.size());
        assertEquals(TYPE_1, caseLogs.get(0).getRequestType());
        assertEquals(TYPE_1, caseLogs.get(1).getRequestType());
    }

    @Test
    public void shouldFilterCaseLogsByEntityIdAndRequestType() {
        List<CaseLog> caseLogs = allCaseLogs.filterByEntityIdAndRequestType("junk", "junk", NUMBER_OF_TEST_LOGS);
        assertTrue(caseLogs.isEmpty());

        caseLogs = allCaseLogs.filterByEntityIdAndRequestType(ID_3, TYPE_2, NUMBER_OF_TEST_LOGS);
        assertNotNull(caseLogs);
        assertEquals(1, caseLogs.size());
        assertEquals(ID_3, caseLogs.get(0).getEntityId());
        assertEquals(TYPE_2, caseLogs.get(0).getRequestType());
    }
}
