package org.motechproject.retry.dao;

import com.google.gson.reflect.TypeToken;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.retry.domain.RetryGroupRecord;
import org.motechproject.retry.domain.RetryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@Repository
public class AllRetriesDefinition {

    private static List<RetryGroupRecord> retryGroupRecords = new ArrayList<RetryGroupRecord>();
    private String json_extension = ".json";

    @Autowired
    public AllRetriesDefinition(@Value("#{retryProperties['retry.definition.dir']}") String definitionsDirectoryName) {
        readSchedulesFromJSON(definitionsDirectoryName);
    }

    private void readSchedulesFromJSON(String definitionsDirectoryName) {
        Type type = new TypeToken<RetryGroupRecord>() {
        }.getType();

        MotechJsonReader motechJsonReader = new MotechJsonReader();

        if(definitionsDirectoryName.startsWith("/")) {
            definitionsDirectoryName = definitionsDirectoryName.substring(1);
        }
        String locationPattern = String.format("classpath*:%s/*%s", definitionsDirectoryName, json_extension);

        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(locationPattern);
            for (Resource resource : resources) {
                RetryGroupRecord retryRecord = (RetryGroupRecord) motechJsonReader.readFromStream(resource.getInputStream(), type);
                retryGroupRecords.add(retryRecord);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public RetryRecord getRetryRecord(String retryScheduleName) {
        List<RetryRecord> map = flatten(extract(retryGroupRecords, on(RetryGroupRecord.class).getRetries()));
        List<RetryRecord> retryRecords = select(map, having(on(RetryRecord.class).name(), equalTo(retryScheduleName)));
        return CollectionUtils.isEmpty(retryRecords) ? null : retryRecords.get(0);
    }

    public RetryRecord getNextRetryRecord(String retryScheduleName) {
        RetryGroupRecord retryGroupRecord = getRetryGroup(retryScheduleName);
        boolean selected = false;
        for (RetryRecord retryRecord : retryGroupRecord.getRetries()) {
            if (selected) return retryRecord;
            if (retryRecord.name().equals(retryScheduleName)) selected = true;
        }
        return null;
    }

    public List<String> getAllRetryRecordNames(String name) {
        List<RetryGroupRecord> lists = select(retryGroupRecords, having(on(RetryGroupRecord.class).getName(), is(name)));
        List<RetryRecord> retryList = flatten(extract(lists, on(RetryGroupRecord.class).getRetries()));
        return extract(retryList, on(RetryRecord.class).name());
    }

    public RetryGroupRecord getRetryGroup(String retryName) {
        for (RetryGroupRecord retryGroupRecord : retryGroupRecords) {
            for (RetryRecord retryRecord : retryGroupRecord.getRetries()) {
                if (retryRecord.name().equals(retryName)) return retryGroupRecord;
            }
        }
        return null;
    }
}
