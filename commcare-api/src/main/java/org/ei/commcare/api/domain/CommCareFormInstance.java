package org.ei.commcare.api.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.ei.commcare.api.contract.CommCareFormDefinition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommCareFormInstance implements Serializable {
    private static final long serialVersionUID = 42L;

    private String formName;
    private String formId;
    private String submittedTimeStamp;
    private Map<String, String> fieldsWeCareAbout;
    private Map<String, Map<String, String>> extraData;
    private boolean hasExtraDataEnabled;

    public CommCareFormInstance(CommCareFormDefinition formDefinition, CommCareFormContent content) {
        this.formName = formDefinition.name();
        this.fieldsWeCareAbout = content.getValuesOfFieldsSpecifiedByPath(formDefinition.mappings());
        this.formId = content.formId();
        this.submittedTimeStamp = content.formSubmitTimeStamp();

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

    public String submittedTimeStamp() {
        return submittedTimeStamp;
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

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
