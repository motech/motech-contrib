package org.motechproject.adherence.repository;

import org.ektorp.CouchDbConnector;
import org.junit.After;
import org.junit.Test;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.testutils.SpringIntegrationTest;
import org.motechproject.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"classpath:applicationAdherenceContext.xml"})
public class AllAdherenceLogsIT extends SpringIntegrationTest {

    @Autowired
    private AllAdherenceLogs allAdherenceLogs;

    @Autowired
    @Qualifier("adherenceDbConnector")
    private CouchDbConnector dbConnector;

    @Test
    public void shouldSaveAdherenceLog() {
        AdherenceLog log = new AdherenceLog("externalId", "referenceId", DateUtil.today());
        log.recordAdherence(DateUtil.today(), DateUtil.tomorrow(), 1, 1);
        allAdherenceLogs.upsert(log);
        System.out.println(allAdherenceLogs.get(log.getId()).externalId());
    }

    @After
    public void tearDown() {
        try {
            for (AdherenceLog log : allAdherenceLogs.getAll()) {
                dbConnector.delete(log);
            }
        } catch (Exception e) {

        }
    }
}
