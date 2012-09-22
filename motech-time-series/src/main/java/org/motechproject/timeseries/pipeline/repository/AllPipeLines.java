package org.motechproject.timeseries.pipeline.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.timeseries.pipeline.contract.PipeLine;
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
        if (entity.getPipeLine().isValid()) {
            super.add(entity);
        }
    }

    public List<PipeLine> allPipeLinesWithSimpleType(String externalId, String typeName) {
        return null;
    }

    public List<PipeLine> allPipeLinesWithTypeWithParameters(String externalId, String typeName) {
        return null;
    }
}
