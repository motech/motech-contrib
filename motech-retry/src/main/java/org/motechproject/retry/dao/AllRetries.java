package org.motechproject.retry.dao;

import com.google.gson.reflect.TypeToken;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;
import org.joda.time.DateTime;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.retry.domain.Retry;
import org.motechproject.retry.domain.RetryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;

@Repository
public class AllRetries extends MotechBaseRepository<Retry> {
    private String definitionsDirectoryName;
    private List<String> definitionFileNames;
    private MotechJsonReader motechJsonReader;
    private static List<RetryRecord> retrySchedules = new ArrayList<RetryRecord>();
    private String json_extension = ".json";

    @Autowired
    public AllRetries(@Value("#{retryProperties['retry.definition.dir']}") String definitionsDirectoryName,
                      @Qualifier("retryConnector") CouchDbConnector dbConnector) {
        super(Retry.class, dbConnector);
        this.definitionsDirectoryName = definitionsDirectoryName;
        this.motechJsonReader = new MotechJsonReader();
    }

    public RetryRecord getRetryRecord(String retryScheduleName) {
        List<RetryRecord> retryRecords = select(readSchedulesFromJSON(), having(on(RetryRecord.class).name(), equalTo(retryScheduleName)));
        return CollectionUtils.isEmpty(retryRecords) ? null : retryRecords.get(0);
    }

    private List<RetryRecord> readSchedulesFromJSON() {
        if (CollectionUtils.isEmpty(retrySchedules)) {
            Type type = new TypeToken<List<RetryRecord>>() {
            }.getType();

            String schedulesDirectoryPath = getClass().getResource(definitionsDirectoryName).getPath();
            File[] definitionFiles = new File(schedulesDirectoryPath).listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String filename) {
                    return filename.endsWith(json_extension);
                }
            });

            definitionFileNames = extract(definitionFiles, on(File.class).getName());

            for (String definitionFileName : definitionFileNames) {
                List<RetryRecord> retryRecords = (List<RetryRecord>) motechJsonReader.readFromFile(
                        definitionsDirectoryName + "/" + definitionFileName,
                        type);

                for (RetryRecord retryRecord : retryRecords) {
                    retrySchedules.add(retryRecord);
                }
            }
        }
        return retrySchedules;
    }

    public void createRetry(Retry retry) {
        Retry existingRetry = getActiveRetryWithStartTime(retry.name(), retry.externalId(), retry.startTime());
        if (existingRetry == null) {
            add(retry);
        }
    }

    @View(name = "get_retry_for_externalId_name_startTime", map = "function(doc) { if(doc.type === 'Retry' && doc.retryStatus === 'ACTIVE') emit([doc.externalId, doc.name, doc.startTime], doc) }")
    private Retry getActiveRetryWithStartTime(String name, String externalId, DateTime startTime) {
        List<Retry> retries = queryView("get_retry_for_externalId_name_startTime", ComplexKey.of(externalId, name, startTime));
        return retries.isEmpty() ? null : retries.get(0);
    }

    @View(name = "get_retry_for_externalId_name", map = "function(doc) { if(doc.type === 'Retry' && doc.retryStatus === 'ACTIVE') emit([doc.externalId, doc.name], doc) }")
    public Retry getActiveRetry(String externalId, String name) {
        List<Retry> retries = queryView("get_retry_for_externalId_name", ComplexKey.of(externalId, name));
        return retries.isEmpty() ? null : retries.get(0);
    }
}
