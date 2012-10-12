package org.motechproject.casexml.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.casexml.CaseLog;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class AllCaseLogs extends MotechBaseRepository<CaseLog> {

    @Autowired
    public AllCaseLogs(@Qualifier("caseLogConnector") CouchDbConnector db) {
        super(CaseLog.class, db);
    }
}
