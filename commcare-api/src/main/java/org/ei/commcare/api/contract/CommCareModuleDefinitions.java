package org.ei.commcare.api.contract;

import java.util.ArrayList;
import java.util.List;

public class CommCareModuleDefinitions {
    private String userName;
    private String password;
    private List<CommCareModuleDefinition> modules;

    public List<CommCareFormDefinition> definitions() {
        ArrayList<CommCareFormDefinition> definitions = new ArrayList<CommCareFormDefinition>();
        for (CommCareModuleDefinition module : modules) {
            definitions.addAll(module.definitions());
        }
        return definitions;
    }

    public String userName() {
        return userName;
    }

    public String password() {
        return password;
    }

    public List<CommCareModuleDefinition> modules() {
        return modules;
    }
}
