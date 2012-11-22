package org.motechproject.casexml.service;

import org.motechproject.casexml.domain.CaseLog;
import org.motechproject.casexml.repository.AllCaseLogs;
import org.motechproject.util.StringUtil;
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

    public List<CaseLog> filter(String entityId, String requestType) {

        if (!StringUtil.isNullOrEmpty(entityId) && !StringUtil.isNullOrEmpty(requestType)) {
            return allCaseLogs.filterByEntityIdAndRequestType(entityId, requestType);
        } else {
            return !StringUtil.isNullOrEmpty(entityId) ? allCaseLogs.filterByEntityId(entityId) :
                    (!StringUtil.isNullOrEmpty(requestType) ? allCaseLogs.filterByRequestType(requestType) : getAll());
        }
    }
}
