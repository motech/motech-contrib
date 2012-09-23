package org.motechproject.timeseries.pipeline.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.timeseries.pipeline.service.PipeLineRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AllPipeLines extends MotechBaseRepository<PipeLineRegistration> {

    @Autowired
    public AllPipeLines(@Qualifier("timeSeriesDBConnector") CouchDbConnector db) {
        super(PipeLineRegistration.class, db);
    }

    @Override
    public void add(PipeLineRegistration entity) {
        if (entity.getPipeLine().isValidityDefined()) {
            super.add(entity);
        }
    }

    @View(name = "with_externalId_and_event", map = "function(doc){ if(doc.type==='PipeLineRegistration') { emit([doc.externalId, doc.pipeLine.type.name], doc._id); } }")
    public List<PipeLineRegistration> withExternalIdAndEvent(String externalId, String eventName) {
        ViewQuery query = createQuery("with_externalId_and_event").key(ComplexKey.of(externalId, eventName)).includeDocs(true);
        return db.queryView(query, PipeLineRegistration.class);
    }
}
