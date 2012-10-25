package org.ei.commcare.api.service;

import org.ei.commcare.api.contract.CommCareModuleDefinition;
import org.ei.commcare.api.contract.CommCareModuleDefinitions;
import org.ei.commcare.api.domain.CommCareFormInstance;
import org.ei.commcare.api.util.CommCareImportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommCareModuleImportService {
    private CommCareFormImportService formImportService;
    private CommCareModuleDefinitions moduleDefinitions;

    @Autowired
    public CommCareModuleImportService(CommCareFormImportService formImportService, CommCareImportProperties properties) {
        this.formImportService = formImportService;
        this.moduleDefinitions = properties.moduleDefinitions();
    }

    public List<List<CommCareFormInstance>> fetchFormsForAllModules() {
        List<List<CommCareFormInstance>> instancesForModules = new ArrayList<List<CommCareFormInstance>>();

        for (CommCareModuleDefinition module : moduleDefinitions.modules()) {
            List<CommCareFormInstance> formInstances = formImportService.fetchForms(module.definitions(), moduleDefinitions.commcareBaseUrl(), moduleDefinitions.userName(), moduleDefinitions.password());
            if (!formInstances.isEmpty()) {
                instancesForModules.add(formInstances);
            }
        }

        return instancesForModules;
    }
}
