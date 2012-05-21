package org.motechproject.http.client.integrationtests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.http.client.service.HttpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext.xml")
public class RetryHttpPostTest {

    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    CustomHttpClientEventListener customHttpClientEventListener;

    @Test
    public void shouldRetryPostingInCaseOfHttpError() {
        httpClientService.post("http://shouldfailasthedomaindoesnotexist/", "dummypostdata");
        Assert.assertTrue("Please stop an already running jetty instance for this test to pass.",hasListenerHasBeenCalledMultipleTimes());
    }

    private boolean hasListenerHasBeenCalledMultipleTimes() {
        int sleepTime = 1000;
        for (int counter = 0; counter < 30; counter++) {
            if (customHttpClientEventListener.hasTriedNumberOfTimes(7)) {
                return true;
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return false;
    }
}


