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
        caseLogService.getAll();

        verify(allCaseLogs).getAll();
    }
}
