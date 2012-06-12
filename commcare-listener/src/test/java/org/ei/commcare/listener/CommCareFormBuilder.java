package org.ei.commcare.listener;

import org.ei.commcare.api.contract.CommCareExportUrl;
import org.ei.commcare.api.contract.CommCareFormDefinition;
import org.ei.commcare.api.domain.CommCareFormContent;
import org.ei.commcare.api.domain.CommCareFormInstance;

import java.util.HashMap;
import java.util.Map;

public class CommCareFormBuilder {
    private String formName;
    private HashMap<String, String> mappings;
    private CommCareFormContent content;
    private Map<String, String> extraMappings;

    public CommCareFormBuilder() {
        this.mappings = new HashMap<String, String>();
        this.extraMappings = new HashMap<String, String>();
    }

    public CommCareFormBuilder withName(String formName) {
        this.formName = formName;
        return this;
    }

    public CommCareFormBuilder withMapping(String pathToField, String parameterToBeMappedTo) {
        mappings.put(pathToField, parameterToBeMappedTo);
        return this;
    }

    public CommCareFormBuilder withExtraMapping(String pathToField, String parameterToBeMappedTo) {
        extraMappings.put(pathToField, parameterToBeMappedTo);
        return this;
    }

    public CommCareFormBuilder withContent(CommCareFormContent content) {
        this.content = content;
        return this;
    }

    public CommCareFormInstance build() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("nameSpace", "http://some.name/space");
        CommCareExportUrl url = new CommCareExportUrl("http://some.url", params);
        return new CommCareFormInstance(new CommCareFormDefinition(formName, url, mappings, extraMappings), content);
    }
}
