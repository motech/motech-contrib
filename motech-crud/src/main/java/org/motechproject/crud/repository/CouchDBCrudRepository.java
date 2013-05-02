package org.motechproject.crud.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.model.MotechBaseDataObject;

import java.util.List;

public class CouchDBCrudRepository<T extends MotechBaseDataObject> extends MotechBaseRepository<T> implements CrudRepository<T> {

    private Class<T> type;

    public CouchDBCrudRepository(Class type, CouchDbConnector db) {
        super(type, db);
        this.type = type;
    }

    public List<T> getAll(int skip, int limit) {
        ViewQuery q = createQuery("all").skip(skip).limit(limit).includeDocs(true);
        return db.queryView(q, type);
    }

    public int count() {
        ViewQuery q = createQuery("all").includeDocs(false);
        return db.queryView(q).getSize();
    }

    public List<T> findBy(String fieldName, String value) {
        String viewName = String.format("by_%s", fieldName);
        ViewQuery q = createQuery(viewName).key(value).includeDocs(true);
        return db.queryView(q, type);
    }

    public void save(T object) {
        if(object.getId() == null){
            add(object);
        } else {
            update(object);
        }
    }
}
