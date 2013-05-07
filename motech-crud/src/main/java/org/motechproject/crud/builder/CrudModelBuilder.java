package org.motechproject.crud.builder;

import org.motechproject.crud.model.CrudModel;
import org.motechproject.crud.service.CrudActions;

import static java.util.Arrays.asList;

public class CrudModelBuilder {

    CrudModel crudModel = new CrudModel();

    public static CrudModelBuilder couchDBCrudModel(Class entityType){
        return new CrudModelBuilder()
                .hiddenFields("_id", "_rev", "type")
                .defaultValue("type", entityType.getSimpleName())
                .idFieldName("_id");
    }

    public static CrudModelBuilder jpaCrudModel(){
        return new CrudModelBuilder();
    }


    public CrudModelBuilder displayFields(String... fields){
        crudModel.setDisplayFields(asList(fields));
        return this;
    }

    public CrudModelBuilder filterFields(String... fields){
        crudModel.setFilterFields(asList(fields));
        return this;
    }

    public CrudModelBuilder hiddenFields(String... fields){
        crudModel.setHiddenFields(asList(fields));
        return this;
    }

    public CrudModelBuilder idFieldName(String idFieldName){
        crudModel.setIdFieldName(idFieldName);
        return this;
    }

    public CrudModelBuilder defaultValue(String key, String value){
        crudModel.addDefaultValue(key, value);
        return this;
    }

    public CrudModelBuilder displayName(String displayName){
        crudModel.setDisplayName(displayName);
        return this;
    }

    public CrudModelBuilder allowActions(CrudActions... actions){
        crudModel.setAllowedActions(asList(actions));
        return this;
    }

    public CrudModel build() {
        return crudModel;
    }
}
