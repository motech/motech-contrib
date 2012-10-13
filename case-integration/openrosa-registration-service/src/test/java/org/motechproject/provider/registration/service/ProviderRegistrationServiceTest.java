package org.motechproject.provider.registration.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.casexml.CaseLog;
import org.motechproject.casexml.repository.AllCaseLogs;
import org.motechproject.provider.registration.impl.SampleProviderRegistrationService;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

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
    AllCaseLogs caseLogs;

    DateTime now = DateUtil.now().withMillisOfSecond(0);

    @Before
    public void setup() {
        mockCurrentDate(now);
        caseLogs.removeAll();
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
        CaseLog caseLog = caseLogs.getAll().get(0);
        assertCaseLog(contextPath, hasException, true, caseLog);
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

}
