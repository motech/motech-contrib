package org.motechproject.timeseries.rules.repository;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.timeseries.rules.entity.TimeSeriesSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AllTimeSeriesSubscriptions extends MotechBaseRepository<TimeSeriesSubscription> {

    @Autowired
    public AllTimeSeriesSubscriptions(@Qualifier("timeSeriesDBConnector") CouchDbConnector db) {
        super(TimeSeriesSubscription.class, db);
    }

    @View(name = "by_externalId_and_event", map = "function (doc) { if(doc.type === 'TimeSeriesSubscription') { emit([doc.externalId, doc.event], doc.ruleName); }}")
    public List<String> ruleNames(String externalId, String trigger) {
        ViewQuery query = createQuery("by_externalId_and_event").key(ComplexKey.of(externalId, trigger));
        ViewResult viewResult = db.queryView(query);

        List<String> names = new ArrayList<>();
        for (ViewResult.Row row : viewResult.getRows()) {
            names.add(row.getValue());
        }
        return names;
    }
}
