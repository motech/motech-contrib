package org.motechproject.paginator.controller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.web.servlet.DispatcherServlet;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-applicationContext-Paginator-mvc.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class PaginationControllerIT {

    @Autowired
    private PaginationController debugOutputController;

    private MockMvc mvc;
    private Server server;

    @Before
    public void setUp() throws Exception {
        server = new Server(7080);
        Context context = new Context(server, "/paginator");

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath:test-applicationContext-Paginator-mvc.xml");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("motech-paginator/src/main/webapp");

        ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
        context.addServlet(servletHolder, "/*");
        server.addHandler(resourceHandler);
        server.setHandler(context);
        server.start();
    }

    @Test
    public void shouldRespondWithNotFoundForEntityWithNoServiceFound() throws Exception {
        HttpClient client = new HttpClient();
        int code = client.executeMethod(new GetMethod("http://localhost:7080/paginator/page/invalidService"));
        assertEquals(404, code);
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
    }
}
