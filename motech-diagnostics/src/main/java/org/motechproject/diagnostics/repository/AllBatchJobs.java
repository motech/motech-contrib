package org.motechproject.diagnostics.repository;

import org.motechproject.diagnostics.model.BatchJob;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class AllBatchJobs {

    public List<BatchJob> fetchAll() {
        return Collections.emptyList();
    }
}
