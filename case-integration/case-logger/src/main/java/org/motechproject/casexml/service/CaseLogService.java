package org.motechproject.casexml.service;

import org.motechproject.casexml.domain.CaseLog;
import org.motechproject.casexml.repository.AllCaseLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.motechproject.util.StringUtil.isNullOrEmpty;

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

    public List<CaseLog> getLatestLogs(int limit) {
        return allCaseLogs.getLatestLogs(limit);
    }

    public List<CaseLog> filter(String entityId, String requestType, int limit) {
        if (!isNullOrEmpty(entityId) && !isNullOrEmpty(requestType)) {
            return allCaseLogs.filterByEntityIdAndRequestType(entityId, requestType, limit);
        } else {
            return !isNullOrEmpty(entityId) ? allCaseLogs.filterByEntityId(entityId, limit) :
                    (!isNullOrEmpty(requestType) ? allCaseLogs.filterByRequestType(requestType, limit) : Collections.<CaseLog>emptyList());
        }
    }
}
