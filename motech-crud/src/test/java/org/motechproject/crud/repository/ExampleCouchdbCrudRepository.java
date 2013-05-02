package org.motechproject.crud.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.GenerateView;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExampleCouchdbCrudRepository  extends MotechBaseRepository<Example> {

    @Autowired
    protected ExampleCouchdbCrudRepository(CouchDbConnector couchDbConnector) {
        super(Example.class, couchDbConnector);
    }

    @GenerateView
    public List<Example> findByField(String field) {
        return queryView("by_field", field);
    }
}
