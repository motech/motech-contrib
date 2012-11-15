package org.motechproject.diagnosticsweb.velocity.builder;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.motechproject.diagnostics.configuration.DiagnosticConfiguration;
import org.motechproject.diagnosticsweb.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class LogFilesResponseBuilder {

    private VelocityEngine velocityEngine;
    private DiagnosticConfiguration diagnosticConfiguration;

    @Autowired
    public LogFilesResponseBuilder(VelocityEngine velocityEngine, DiagnosticConfiguration diagnosticConfiguration) {
        this.velocityEngine = velocityEngine;
        this.diagnosticConfiguration = diagnosticConfiguration;
    }

    public String createResponse() {
        Template template = velocityEngine.getTemplate("diagnostics-web/views/content/logs.vm");
        VelocityContext context = new VelocityContext();
        context.put("contextPath", diagnosticConfiguration.contextPath());
        context.put("logFilesInfo", getLogFiles());
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    public FileInputStream getLogFile(final String logFilename) throws FileNotFoundException {
        File logDirectory = new File(diagnosticConfiguration.logFileLocation());
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

    private List<FileInfo> getLogFiles() {
        File logDirectory = new File(diagnosticConfiguration.logFileLocation());
        List<FileInfo> logFilesInfo = new ArrayList<>();
        if(logDirectory.exists()){
            File[] logs = logDirectory.listFiles();
            for (File log : logs)
                logFilesInfo.add(new FileInfo(log.getName(), log.length(), log.lastModified()));
        }
        Collections.sort(logFilesInfo);
        return logFilesInfo;
    }
}
