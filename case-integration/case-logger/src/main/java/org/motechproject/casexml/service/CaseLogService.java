package org.motechproject.casexml.service;

import org.motechproject.casexml.domain.CaseLog;
import org.motechproject.casexml.repository.AllCaseLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseLogService {
    private AllCaseLogs allCaseLogs;

    @Autowired
    public CaseLogService(AllCaseLogs allCaseLogs) {
        this.allCaseLogs = allCaseLogs;
    }

    public void add(CaseLog caseLog) {
        allCaseLogs.add(caseLog);
    }

    public List<CaseLog> getAll() {
        return allCaseLogs.getAll();
    }
}
