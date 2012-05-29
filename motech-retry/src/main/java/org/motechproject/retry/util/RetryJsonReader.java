package org.motechproject.retry.util;

import com.google.gson.reflect.TypeToken;
import org.motechproject.dao.MotechJsonReader;
import org.motechproject.retry.domain.RetryJsonRecord;
import org.motechproject.retry.domain.RetryRecord;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class RetryJsonReader {
    private String definitionsDirectoryName;
    private List<String> definitionFileNames;
    private MotechJsonReader motechJsonReader;
    private static List<RetryJsonRecord> retryJsonRecords = new ArrayList<RetryJsonRecord>();
    private String json_extension = ".json";

    public RetryJsonReader(String definitionsDirectoryName) {
        this.definitionsDirectoryName = definitionsDirectoryName;
        motechJsonReader = new MotechJsonReader();
    }

    private List<RetryJsonRecord> readSchedulesFromJSON() {
        if (CollectionUtils.isEmpty(retryJsonRecords)) {
            Type type = new TypeToken<RetryJsonRecord>() {
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
                RetryJsonRecord retryRecord = (RetryJsonRecord) motechJsonReader.readFromFile(
                        definitionsDirectoryName + "/" + definitionFileName,
                        type);
                retryJsonRecords.add(retryRecord);
            }
        }
        return retryJsonRecords;
    }

    public RetryRecord getRetryRecord(String retryScheduleName) {
        List<RetryRecord> map = flatten(extract(readSchedulesFromJSON(), on(RetryJsonRecord.class).getRetries()));
        List<RetryRecord> retryRecords = select(map, having(on(RetryRecord.class).name(), equalTo(retryScheduleName)));
        return CollectionUtils.isEmpty(retryRecords) ? null : retryRecords.get(0);
    }

    public RetryRecord getNextRetryRecord(String retryScheduleName) {
        for (RetryJsonRecord retryJsonRecord : readSchedulesFromJSON()) {
            boolean selected = false;
            for (RetryRecord retryRecord : retryJsonRecord.getRetries()) {
                if (selected) return retryRecord;
                if (retryRecord.name().equals(retryScheduleName)) selected = true;
            }
        }
        return null;
    }

    public List<String> getAllRetryRecordNames(String name) {
        List<RetryJsonRecord> lists = select(readSchedulesFromJSON(), having(on(RetryJsonRecord.class).getName(), is(name)));
        List<RetryRecord> retryList = flatten(extract(lists, on(RetryJsonRecord.class).getRetries()));
        return extract(retryList, on(RetryRecord.class).name());
    }

    public String getRetryGroupName(String retryName) {
        for (RetryJsonRecord retryJsonRecord : readSchedulesFromJSON()) {
            for (RetryRecord retryRecord : retryJsonRecord.getRetries()) {
                if (retryRecord.name().equals(retryName)) return retryJsonRecord.getName();
            }
        }
        return null;
    }
}
