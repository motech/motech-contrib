package org.motechproject.crud.builder;

import org.apache.commons.lang.StringUtils;
import org.ektorp.CouchDbConnector;
import org.motechproject.crud.event.CrudEventHandler;
import org.motechproject.crud.repository.CouchDBCrudRepository;
import org.motechproject.crud.repository.CrudRepository;
import org.motechproject.crud.repository.JpaCrudRepository;
import org.motechproject.crud.service.CrudEntity;
import org.motechproject.crud.service.CrudEntityImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public class CrudEntityBuilder {

    public static final String MANDATORY_FIELDS_MESSAGE = "crudRepository, crudModelBuilder and entityType are mandatory.";
    private CrudRepository crudRepository = null;
    private CrudModelBuilder crudModelBuilder = null;
    private CrudEventHandler eventHandler;
    private Class entityType = null;

    public CrudEntityBuilder(Class entityType) {
        this.entityType = entityType;
    }

    public static CrudEntityBuilder newCrudEntity(Class entityType){
        return new CrudEntityBuilder(entityType);
    }

    public CrudEntityBuilder crudRepository(CrudRepository crudRepository){
        this.crudRepository = crudRepository;
        return this;
    }

    public CrudEntityBuilder couchDBCrudRepository(CouchDbConnector couchDbConnector){
        this.crudRepository = new CouchDBCrudRepository(entityType, couchDbConnector);
        return this;
    }

    public CrudEntityBuilder jpaCrudRepository(JpaRepository jpaRepository, Class idClass){
        this.crudRepository = new JpaCrudRepository(jpaRepository, idClass);
        return this;
    }

    public CrudEntityBuilder model(CrudModelBuilder modelBuilder){
        crudModelBuilder = modelBuilder;
        if(crudModelBuilder.crudModel.getDisplayName() == null){
            crudModelBuilder.displayName(StringUtils.join(entityType.getSimpleName().split("(?=[A-Z])"), " ").trim());
        }
        return this;
    }

    public CrudEntityBuilder eventHandler(CrudEventHandler eventHandler){
        this.eventHandler = eventHandler;
        return this;
    }

    public CrudEntity build(){
        if(crudRepository == null || crudModelBuilder == null || entityType == null){
            throw new RuntimeException(MANDATORY_FIELDS_MESSAGE);
        }

        return new CrudEntityImpl(crudRepository, crudModelBuilder.build(), entityType, eventHandler);
    }
}
