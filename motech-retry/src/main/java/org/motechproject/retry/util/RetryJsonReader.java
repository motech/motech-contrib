package org.motechproject.retry.util;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.MapUtils;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.retry.domain.RetryRecord;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class RetryJsonReader {
    private String definitionsDirectoryName;
    private List<String> definitionFileNames;
    private MotechJsonReader motechJsonReader;
    private static Map<String, List<RetryRecord>> retryJsonRecords = new HashMap<String, List<RetryRecord>>();
    private String json_extension = ".json";

    public RetryJsonReader(String definitionsDirectoryName) {
        this.definitionsDirectoryName = definitionsDirectoryName;
        motechJsonReader = new MotechJsonReader();
    }

    private Map<String, List<RetryRecord>> readSchedulesFromJSON() {
        if (CollectionUtils.isEmpty(retryJsonRecords)) {
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
                    List<RetryRecord> list = CollectionUtils.isEmpty(retryJsonRecords.get(definitionFileName)) ? new ArrayList<RetryRecord>() : retryJsonRecords.get(definitionFileName);
                    list.add(retryRecord);
                    MapUtils.safeAddToMap(retryJsonRecords, definitionFileName, list);
                }
            }
        }
        return retryJsonRecords;
    }

    public RetryRecord getRetryRecord(String retryScheduleName) {
        List<RetryRecord> map = flatten(readSchedulesFromJSON());
        List<RetryRecord> retryRecords = select(map, having(on(RetryRecord.class).name(), equalTo(retryScheduleName)));
        return CollectionUtils.isEmpty(retryRecords) ? null : retryRecords.get(0);
    }
}
