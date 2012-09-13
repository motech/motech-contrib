package org.motechproject.diagnostics.repository;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.motechproject.diagnostics.model.BatchJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-DiagnosticsTest.xml")
public class AllBatchJobsIT {

    @Autowired
    AllBatchJobs allBatchJobs;

    private Server springBatchServer;

    @Before
    public void setUp() throws Exception {
        springBatchServer = new Server(9123);
        Context context = new Context(springBatchServer, "/springbatch");

        createServlet(context, "/jobs.json", "spring-batch-jobs.json");
        createServlet(context, "/jobs/viewIndexerJob.json", "view-indexer-job.json");
        createServlet(context, "/jobs/dbCompactorJob.json", "db-compactor-job.json");
        springBatchServer.setHandler(context);
        springBatchServer.start();
    }

    @After
    public void tearDown() throws Exception {
        springBatchServer.stop();
    }

    @Test
    public void shouldFetchAllJobExecution() throws Exception {
        List<BatchJob> batchJobs = allBatchJobs.fetchAll();

        assertEquals("viewIndexerJob", batchJobs.get(0).getName());
        assertEquals("dbCompactorJob", batchJobs.get(1).getName());
    }

    public void createServlet(Context context, String urlPath, final String responseFileName){
        Servlet allJobsServlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                InputStream resource = this.getClass().getClassLoader().getResourceAsStream("batchjob/" + responseFileName);
                resp.setContentType("application/json");
                resp.getWriter().print(IOUtils.toString(resource));
            }
        };

        ServletHolder servletHolder = new ServletHolder(allJobsServlet);
        context.addServlet(servletHolder, urlPath);
    }
}
