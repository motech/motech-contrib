package service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.casexml.domain.CaseLog;
import org.motechproject.casexml.repository.AllCaseLogs;
import org.motechproject.casexml.service.CaseLogService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CaseLogServiceTest {

    @Mock
    private AllCaseLogs allCaseLogs;
    private CaseLogService caseLogService;

    @Before
    public void setUp() {
        initMocks(this);
        caseLogService = new CaseLogService(allCaseLogs);
    }

    @Test
    public void shouldAddCaseLog() {
        CaseLog caseLog = mock(CaseLog.class);

        caseLogService.add(caseLog);

        verify(allCaseLogs).add(caseLog);
    }

    @Test
    public void shouldGetAllCaseLogs() {
        caseLogService.getLatestLogs(2);

        verify(allCaseLogs).getLatestLogs(2);
    }

    @Test
    public void shouldFilterCaseLogsByEntityId() {
        String entityId = "entityId";
        caseLogService.filter(entityId, null, 2);
        verify(allCaseLogs).filterByEntityId(entityId, 2);
    }

    @Test
    public void shouldFilterCaseLogsByRequestType() {
        String requestType = "requestType";
        caseLogService.filter(null, requestType, 2);
        verify(allCaseLogs).filterByRequestType(requestType, 2);
    }

    @Test
    public void shouldFilterCaseLogsByEntityIdAndRequestType() {
        String entityId = "entityId";
        String requestType = "requestType";
        caseLogService.filter(entityId, requestType, 2);
        verify(allCaseLogs).filterByEntityIdAndRequestType(entityId, requestType, 2);
    }
}
