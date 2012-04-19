package org.ei.commcare.api.contract;

import java.util.List;

public class CommCareModuleDefinition {
    private List<CommCareFormDefinition> forms;

    public List<CommCareFormDefinition> definitions() {
        return forms;
    }
}
