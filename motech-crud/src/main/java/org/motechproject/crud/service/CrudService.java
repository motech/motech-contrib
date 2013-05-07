package org.motechproject.crud.service;

import org.motechproject.crud.repository.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class CrudService {
    Map<String, CrudEntity> allCrudEntities;

    @Autowired(required = false)
    public CrudService(Set<CrudEntity> crudEntities) {
        this();
        for (CrudEntity crudEntity : crudEntities) {
            allCrudEntities.put(crudEntity.entityName(), crudEntity);
        }
    }

    public CrudService() {
        allCrudEntities = new HashMap<>();
    }

    public void deleteEntity(String name, String entityId) {
        CrudEntity crudEntity = getCrudEntity(name);
        CrudRepository repository = crudEntity.getRepository();
        Object object = repository.get(entityId);
        repository.remove(object);
        crudEntity.deleted(object);
    }

    public void saveEntity(String name, Object object) {
        CrudEntity crudEntity = getCrudEntity(name);
        CrudRepository repository = crudEntity.getRepository();
        repository.save(object);
        crudEntity.updated(object);
    }

    public Object getEntity(String entityName, String entityId) {
        CrudRepository repository = getCrudEntity(entityName).getRepository();
        return repository.get(entityId);
    }

    public CrudEntity getCrudEntity(String name) {
        return allCrudEntities.get(name);
    }
}
