package org.motechproject.diagnostics.page;

import org.motechproject.diagnostics.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LogsPage {
    private String view = "admin/logs";
    private String peerView = "admin/peer_logs";

    private File logDirectory;

    @Autowired
    public LogsPage(@Value("${log.location}") String logLocation) {
        this.logDirectory = new File(logLocation);
    }

    public ModelAndView display() {
        return displayLogsWithView(view);
    }

    public ModelAndView displayAsPeerLogs() {
        return displayLogsWithView(peerView);
    }

    public FileInputStream getLogFile(final String logFilename) throws FileNotFoundException {
        File[] files = logDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.equals(logFilename);
            }
        });
        if (files.length > 0)
            return new FileInputStream(files[0]);

        throw new FileNotFoundException(String.format("%s log file not found", logFilename));
    }

    private ModelAndView displayLogsWithView(String view) {
        List<FileInfo> logFilesInfo = new ArrayList<FileInfo>();
        if(logDirectory.exists()){
            File[] logs = logDirectory.listFiles();
            for (File log : logs)
                logFilesInfo.add(new FileInfo(log.getName(), log.length(), log.lastModified()));
        }
        Collections.sort(logFilesInfo);

        return new ModelAndView(view)
                .addObject("logFilesInfo", logFilesInfo);
    }
}
