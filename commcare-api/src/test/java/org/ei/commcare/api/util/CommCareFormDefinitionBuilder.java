package org.ei.commcare.api.util;

import org.ei.commcare.api.contract.CommCareExportUrl;
import org.ei.commcare.api.contract.CommCareFormDefinition;

import java.util.HashMap;

public class CommCareFormDefinitionBuilder {
    public static CommCareFormDefinition createForm(String formPrefix, String nameSpace) {
        HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("nameSpace", nameSpace);
        CommCareExportUrl url = new CommCareExportUrl("http://baseURL" + formPrefix, queryParams);

        HashMap<String, String> mappings = new HashMap<String, String>();
        mappings.put("form|path|to|field", "FieldInOutput");
        mappings.put("form|path|to|another|field", "AnotherFieldInOutput");
        return new CommCareFormDefinition(formPrefix, url, mappings);
    }
}
