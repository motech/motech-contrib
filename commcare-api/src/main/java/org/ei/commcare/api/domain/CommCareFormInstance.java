package org.ei.commcare.api.domain;

import org.ei.commcare.api.contract.CommCareFormDefinition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommCareFormInstance implements Serializable {
    private static final long serialVersionUID = 42L;

    private String formName;
    private String formId;
    private Map<String, String> fieldsWeCareAbout;
    private Map<String, Map<String, String>> extraData;
    private boolean hasExtraDataEnabled;

    public CommCareFormInstance(CommCareFormDefinition formDefinition, CommCareFormContent content) {
        this.formName = formDefinition.name();
        this.fieldsWeCareAbout = content.getValuesOfFieldsSpecifiedByPath(formDefinition.mappings());
        this.formId = content.formId();

        this.extraData = new HashMap<>();
        for (String extraMappingKey : formDefinition.extraMappings().keySet()) {
            extraData.put(extraMappingKey, content.getValuesOfFieldsSpecifiedByPath(formDefinition.extraMappings().get(extraMappingKey)));
        }
        this.hasExtraDataEnabled = !formDefinition.extraMappings().isEmpty();
    }

    public String formName() {
        return formName;
    }

    public String formId() {
        return formId;
    }

    public Map<String, String> fields() {
        return fieldsWeCareAbout;
    }

    public Map<String, Map<String, String>> extraData() {
        return extraData;
    }

    public boolean hasExtraDataEnabled() {
        return hasExtraDataEnabled;
    }
}
