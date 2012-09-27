package org.motechproject.timeseries.rules.repository;

import org.ektorp.AttachmentInputStream;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.timeseries.rules.entity.TimeSeriesRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Repository
public class AllTimeSeriesRules extends MotechBaseRepository<TimeSeriesRule> {

    @Autowired
    public AllTimeSeriesRules(@Qualifier("timeSeriesDBConnector") CouchDbConnector db) {
        super(TimeSeriesRule.class, db);
    }

    public void addContent(TimeSeriesRule rule) {
        try {
            add(rule);
            db.createAttachment(rule.getId(), rule.getRevision(), createAttachment(rule));
        } finally {
            closeInputStream(rule);
        }
    }

    @View(name = "by_externalId_and_trigger", map = "function (doc) { if(doc.type === 'TimeSeriesRule') { emit([doc.externalId, doc.trigger], doc._id); }}")
    public List<TimeSeriesRule> withTrigger(String externalId, String trigger) {
        ViewQuery query = createQuery("by_externalId_and_trigger").key(ComplexKey.of(externalId, trigger)).includeDocs(true);
        List<TimeSeriesRule> result = db.queryView(query, TimeSeriesRule.class);

        if (result == null || result.isEmpty()) return Collections.emptyList();

        for (TimeSeriesRule timeSeriesRule : result) {
            AttachmentInputStream attachmentInputStream = db.getAttachment(timeSeriesRule.getId(), timeSeriesRule.getId());
            timeSeriesRule.setInputStream(attachmentInputStream);
        }
        return result;
    }

    private AttachmentInputStream createAttachment(TimeSeriesRule rule) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(rule.getInputStream());
        return new AttachmentInputStream(rule.getId(), bufferedInputStream, rule.getContentType());
    }

    private void closeInputStream(TimeSeriesRule content) {
        try {
            if (content != null) {
                content.getInputStream().close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
