package org.ei.commcare.api.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import org.ei.commcare.api.contract.CommCareFormDefinition;
import org.ei.commcare.api.domain.CommCareFormContent;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.api.domain.ExportToken;
import org.ei.commcare.api.repository.AllExportTokens;
import org.ei.commcare.api.util.CommCareHttpClient;
import org.ei.commcare.api.util.CommCareHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommCareFormImportService {
    private final CommCareHttpClient httpClient;
    private AllExportTokens allExportTokens;
    private static Logger logger = LoggerFactory.getLogger(CommCareFormImportService.class.toString());

    @Autowired
    public CommCareFormImportService(AllExportTokens allExportTokens, CommCareHttpClient httpClient) {
        this.httpClient = httpClient;
        this.allExportTokens = allExportTokens;
    }

    public List<CommCareFormInstance> fetchForms(List<CommCareFormDefinition> definitions, String commcareBaseUrl, String userName, String password) {
        List<CommCareFormInstance> formInstances = processAllForms(fetchAllForms(definitions, commcareBaseUrl, userName, password));
        logger.info("Fetched " + formInstances.size() + " formInstances.");
        return formInstances;
    }

    private List<CommCareFormWithResponse> fetchAllForms(List<CommCareFormDefinition> definitions, String commcareBaseUrl, String userName, String password) {
        List<CommCareFormWithResponse> formWithResponses = new ArrayList<CommCareFormWithResponse>();
        ArrayList<ExportToken> exportTokens = new ArrayList<ExportToken>();

        for (CommCareFormDefinition formDefinition : definitions) {
            String previousToken = allExportTokens.findByNameSpace(formDefinition.nameSpace()).value();
            CommCareHttpResponse responseFromCommCareHQ = httpClient.get(formDefinition.url(previousToken), commcareBaseUrl, userName, password);

            if (responseFromCommCareHQ.isFailure()) {
                logger.warn("Fetching forms for module failed. Form which failed: " + formDefinition + ". Description: " + responseFromCommCareHQ);
                return new ArrayList<CommCareFormWithResponse>();
            }

            if (responseFromCommCareHQ.hasValidExportToken()) {
                exportTokens.add(new ExportToken(formDefinition.nameSpace(), responseFromCommCareHQ.tokenForNextExport()));
                formWithResponses.add(new CommCareFormWithResponse(formDefinition, responseFromCommCareHQ));
            }
        }

        for (ExportToken exportToken : exportTokens) {
            allExportTokens.updateToken(exportToken.nameSpace(), exportToken.value());
        }

        return formWithResponses;
    }

    private List<CommCareFormInstance> processAllForms(List<CommCareFormWithResponse> careFormWithResponses) {
        List<CommCareFormInstance> formInstances = new ArrayList<CommCareFormInstance>();
        for (CommCareFormWithResponse formWithResponse : careFormWithResponses) {
            CommCareFormDefinition definition = formWithResponse.formDefinition;
            CommCareHttpResponse response = formWithResponse.response;

            CommCareExportedForms exportedFormData;
            try {
                exportedFormData = new Gson().fromJson(response.contentAsString(), CommCareExportedForms.class);
            } catch (JsonParseException e) {
                throw new RuntimeException(response.contentAsString() + e);
            }
            for (List<String> formData : exportedFormData.formContents()) {
                formInstances.add(new CommCareFormInstance(definition, new CommCareFormContent(exportedFormData.headers(), formData)));
            }
        }

        return formInstances;
    }

    private class CommCareFormWithResponse {
        private final CommCareFormDefinition formDefinition;
        private final CommCareHttpResponse response;

        public CommCareFormWithResponse(CommCareFormDefinition formDefinition, CommCareHttpResponse response) {
            this.formDefinition = formDefinition;
            this.response = response;
        }
    }

    private static class CommCareExportedForms {
        @SerializedName("#")
        private CommCareExportedHeadersAndContent content;

        private static class CommCareExportedHeadersAndContent {
            private List<String> headers;
            private List<List<String>> rows;
        }

        public List<String> headers() {
            return content.headers;
        }

        public List<List<String>> formContents() {
            return content.rows;
        }

    }
}
