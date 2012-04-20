package org.motechproject.adherence.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AllAdherenceLogs extends MotechBaseRepository<AdherenceLog> {

    @Autowired
    protected AllAdherenceLogs(@Qualifier("adherenceDbConnector") CouchDbConnector db) {
        super(AdherenceLog.class, db);
    }

    public void upsert(AdherenceLog logToInsert) {
        AdherenceLog latestLog = find(logToInsert.externalId(), logToInsert.referenceId(), logToInsert.year());
        if (latestLog == null) {
            add(logToInsert);
        } else {
            update(logToInsert);
        }
    }

    @View(name = "find_by_year",
            map = "  function(doc){ " +
                    "   if(doc.type === 'AdherenceLog'){" +
                    "     emit([doc.externalId, doc.referenceId, doc.year], doc._id);" +
                    "   }" +
                    "}"
    )
    public AdherenceLog find(String externalId, String referenceId, Integer year) {
        ViewQuery query = createQuery("find_by_year").key(ComplexKey.of(externalId, referenceId, year)).includeDocs(true);
        return singleResult(db.queryView(query, AdherenceLog.class));
    }
}
