package org.motechproject.diagnostics.diagnostics;

import org.apache.commons.dbcp.BasicDataSource;
import org.motechproject.diagnostics.Diagnostics;
import org.motechproject.diagnostics.annotation.Diagnostic;
import org.motechproject.diagnostics.response.DiagnosticsResult;
import org.motechproject.diagnostics.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DBCPDataSourceDiagnostics implements Diagnostics {

    List<BasicDataSource> dataSources;

    @Autowired(required = false)
    public DBCPDataSourceDiagnostics(List<BasicDataSource> dataSources) {
        this.dataSources = dataSources;
    }

    DBCPDataSourceDiagnostics() {
    }

    @Override
    public String name() {
        return DiagnosticServiceName.DBCP;
    }

    @Override
    public boolean canPerformDiagnostics() {
        return dataSources != null;
    }

    @Diagnostic(name = "Statistics")
    public DiagnosticsResult diagnostics() {
        List<DiagnosticsResult> dataSourceResults = new ArrayList<>();
        for(BasicDataSource dataSource : dataSources){
            List<DiagnosticsResult> dataSourceStatistics = new ArrayList<>();
            dataSourceStatistics.add(new DiagnosticsResult("Num Active", String.valueOf(dataSource.getNumActive()), Status.Success));
            dataSourceStatistics.add(new DiagnosticsResult("Num Idle", String.valueOf(dataSource.getNumIdle()), Status.Success));
            dataSourceStatistics.add(new DiagnosticsResult("Max Active", String.valueOf(dataSource.getMaxActive()), Status.Success));
            dataSourceStatistics.add(new DiagnosticsResult("Max Idle", String.valueOf(dataSource.getMaxIdle()), Status.Success));
            dataSourceStatistics.add(new DiagnosticsResult("Max Wait Time", String.valueOf(dataSource.getMaxWait()), Status.Success));
            dataSourceStatistics.add(new DiagnosticsResult("Min Evicatable Idle Time", String.valueOf(dataSource.getMinEvictableIdleTimeMillis()), Status.Success));
            dataSourceStatistics.add(new DiagnosticsResult("Time Betweeen Eviction", String.valueOf(dataSource.getTimeBetweenEvictionRunsMillis()), Status.Success));

            dataSourceResults.add(new DiagnosticsResult(dataSource.getUrl(), dataSourceStatistics));
        }
        return new DiagnosticsResult("DBCP Statistics", dataSourceResults);
    }
}
