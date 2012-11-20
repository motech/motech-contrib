package org.motechproject.caselogs.velocity.builder;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.casexml.domain.CaseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-CaseLoggerWeb.xml")
public class CaseLogsResponseBuilderIT {

    @Autowired
    CaseLogsResponseBuilder responseBuilder;

    @Test
    public void shouldBuildResponseFromCaseLogs() {
        List<CaseLog> caseLogs = new ArrayList<>();
        CaseLog caseLog1 = new CaseLog("body", "url", false, DateTime.now());
        CaseLog caseLog2 = new CaseLog("another body", "another url", false, DateTime.now());
        caseLogs.add(caseLog1);
        caseLogs.add(caseLog2);

        String responseMessage = responseBuilder.createResponseMessage(caseLogs);

        assertTrue(responseMessage.contains("body"));
        assertTrue(responseMessage.contains("another body"));
        assertTrue(responseMessage.contains("whp-test"));
    }
}
