package org.motechproject.retry.dao;

import com.google.gson.reflect.TypeToken;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.retry.domain.RetryRecord;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AllRetries {
    private String definitionsDirectoryName;
    private List<String> definitionFileNames;
    private MotechJsonReader motechJsonReader;
    private static List<RetryRecord> retrySchedules = new ArrayList<RetryRecord>();
    private String json_extension = ".json";

    @Autowired
    public AllRetries(@Value("#{retryProperties['retry.definition.dir']}") String definitionsDirectoryName) {
        this.definitionsDirectoryName = definitionsDirectoryName;
        this.motechJsonReader = new MotechJsonReader();
    }

    private List<RetryRecord> readCampaignsFromJSON() {
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

    public RetryRecord get(String retryScheduleName) {
        List<RetryRecord> retryRecords = select(readCampaignsFromJSON(), having(on(RetryRecord.class).name(), equalTo(retryScheduleName)));
        return CollectionUtils.isEmpty(retryRecords) ? null : retryRecords.get(0);
    }
}
