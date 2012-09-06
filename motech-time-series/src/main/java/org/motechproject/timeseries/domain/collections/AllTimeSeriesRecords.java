package org.motechproject.timeseries.domain.collections;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.timeseries.domain.entities.TimeSeriesRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AllTimeSeriesRecords extends MotechBaseRepository<TimeSeriesRecord> {

    @Autowired
    public AllTimeSeriesRecords(@Qualifier("dbConnector") CouchDbConnector db) {
        super(TimeSeriesRecord.class, db);
    }

    @View(name = "with_externaId", map = "function(doc) {if (doc.type =='TimeSeriesRecord') {emit(doc.externalId, doc._id);}}")
    public TimeSeriesRecord withExternalId(String externalId) {
        ViewQuery q = createQuery("with_externaId").key(externalId).includeDocs(true);
        return singleResult(db.queryView(q, TimeSeriesRecord.class));
    }

}
