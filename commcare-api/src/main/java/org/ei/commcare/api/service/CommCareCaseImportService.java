package org.ei.commcare.api.service;

import com.google.gson.Gson;
import org.ei.commcare.api.contract.CommCareModuleDefinitions;
import org.ei.commcare.api.domain.CaseInformation;
import org.ei.commcare.api.util.CommCareHttpClient;
import org.ei.commcare.api.util.CommCareHttpResponse;
import org.ei.commcare.api.util.CommCareImportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommCareCaseImportService {
    private CommCareHttpClient commCareHttpClient;
    private final CommCareModuleDefinitions moduleDefinitions;

    @Autowired
    public CommCareCaseImportService(CommCareHttpClient commCareHttpClient, CommCareImportProperties commCareImportProperties) {
        this.commCareHttpClient = commCareHttpClient;
        moduleDefinitions = commCareImportProperties.moduleDefinitions();
    }

    public CaseInformation fetchDependentCase(String caseId) {
        CloudCareCaseData motherCaseData = fetchCaseData(caseId);
        if (motherCaseData == null) {
            return new CaseInformation(CaseInformation.Status.FAILED, new HashMap<String, String>());
        }

        CloudCareCaseData childCaseData = fetchCaseData(motherCaseData.reverse_indices.parent.get("case_id"));
        if (childCaseData == null) {
            return new CaseInformation(CaseInformation.Status.FAILED, new HashMap<String, String>());
        }

        return new CaseInformation(CaseInformation.Status.SUCCESS, childCaseData.properties);
    }

    private CloudCareCaseData fetchCaseData(String caseId) {
        CommCareHttpResponse caseFetchResponse = commCareHttpClient.get(moduleDefinitions.caseFetchURLBase() + "?case_id=" + caseId, moduleDefinitions.commcareBaseUrl(), moduleDefinitions.userName(), moduleDefinitions.password());
        if (caseFetchResponse.isFailure()) {
            return null;
        }

        CloudCareCaseData[] caseData = new Gson().fromJson(caseFetchResponse.contentAsString(), CloudCareCaseData[].class);
        if (caseData.length == 0) {
            return null;
        }

        return caseData[0];
    }

    private static class CloudCareCaseData {
        private ReverseIndices reverse_indices;
        private Map<String, String> properties;

        private static class ReverseIndices {
            private Map<String, String> parent;
        }
    }
}
