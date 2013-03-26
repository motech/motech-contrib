package org.motechproject.http.client.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.kubek2k.springockito.annotations.WrapWithSpy;
import org.motechproject.http.client.components.AsynchronousCall;
import org.motechproject.model.MotechEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:applicationContextHttpClient.xml")
public class HttpClientServiceIT {

    @Autowired
    HttpClientService httpClientService;

    @Autowired
    @WrapWithSpy
    private AsynchronousCall asynchronous;  //use qualifier name as property name for Springockito to load bean

    @Test
    public void shouldUseAsynchronousTypeAsDefaultCommunicationType() {
        httpClientService.post("url", "data");
        verify(asynchronous).send(any(MotechEvent.class));
    }
}
