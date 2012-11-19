package org.motechproject.provider.registration.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.casexml.domain.CaseLog;
import org.motechproject.casexml.service.CaseLogService;
import org.motechproject.provider.registration.impl.SampleProviderRegistrationService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext-openrosa.xml")
public class ProviderRegistrationServiceTest extends BaseUnitTest {

    @Autowired
    SampleProviderRegistrationService providerRegistrationService;

    @Autowired
    CaseLogService caseLogService;

    DateTime now = DateUtil.now().withMillisOfSecond(0);

    @Before
    public void setup() {
        toDelete = new ArrayList<>();
        mockCurrentDate(now);
    }

    @Qualifier("caseLogConnector")
    @Autowired
    protected CouchDbConnector caseLogConnector;

    protected ArrayList<BulkDeleteDocument> toDelete;

    @After
    public void after() {
        deleteAll();
    }

    @Test
    public void shouldLogCreateCaseResponses() throws Exception {
        byte[] bytes = readSampleXML("/testCreateXML");
        testLogCreated(bytes, false);
    }

    @Test
    public void shouldLogCreateResponseInCaseOfException() throws Exception {
        byte[] bytes = readSampleXML("/invalidCreateXML");
        testLogCreated(bytes, true);
    }

    private void testLogCreated(byte[] bytes, boolean hasException) throws Exception {
        String contextPath = "/case/process";
        standaloneSetup(providerRegistrationService)
                .build()
                .perform(
                        post(contextPath)
                                .body(bytes)
                );
        CaseLog caseLog = caseLogService.getAll().get(0);
        markForDeletion(caseLog);
        assertCaseLog(contextPath, hasException, true, caseLog);
    }

    private void markForDeletion(CaseLog caseLog) {
        toDelete.add(BulkDeleteDocument.of(caseLog));
    }

    private byte[] readSampleXML(String xmlPath) throws IOException {
        return IOUtils.toByteArray(this.getClass().getResourceAsStream(xmlPath));
    }

    private void assertCaseLog(String contextPath, boolean hasException, boolean hasContent, CaseLog caseLog) {
        assertEquals(contextPath, caseLog.getContextPath());
        assertEquals(hasException, caseLog.getHasException());
        assertEquals(hasContent, StringUtils.isNotBlank(caseLog.getRequest()));
        assertTrue(StringUtils.isNotBlank(caseLog.getResponse()));
        assertEquals(now, caseLog.getLogDate());
    }


    protected void deleteAll() {
        if (toDelete.size() > 0)
            caseLogConnector.executeBulk(toDelete);
        toDelete.clear();
    }
}
