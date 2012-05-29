package org.motechproject.retry.dao;

import org.motechproject.retry.domain.RetryRecord;
import org.motechproject.retry.util.RetryJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllRetriesDefinition {
    private RetryJsonReader retryJsonReader;

    @Autowired
    public AllRetriesDefinition(@Value("#{retryProperties['retry.definition.dir']}") String definitionsDirectoryName) {
        retryJsonReader = new RetryJsonReader(definitionsDirectoryName);
    }

    public RetryRecord getRetryRecord(String retryScheduleName) {
        return retryJsonReader.getRetryRecord(retryScheduleName);
    }

    public RetryRecord getNextRetryRecord(String retryScheduleName) {
        return retryJsonReader.getNextRetryRecord(retryScheduleName);
    }

    public List<String> getAllRetryRecordNames(String name) {
        return retryJsonReader.getAllRetryRecordNames(name);
    }

    public String getRetryGroupName(String retryName) {
        return retryJsonReader.getRetryGroupName(retryName);
    }
}
