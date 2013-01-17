package org.motechproject.jasper.reports.util;

import db.migration.domain.Role;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JasperRESTClientTest {
    private JasperRESTClient jasperRestClient;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        jasperRestClient = new JasperRESTClient(restTemplate);
    }

    @Test
    public void shouldPutRequest() {
        String url = "url";
        Role requestBody = new Role("ROLE1");
        String authorisation = "jasperadmin:jasperadmin";
        byte[] encodedBytes = new Base64().encode(authorisation.getBytes());
        String expectedAuthorisationHeader = "Basic " + new String(encodedBytes).replace("\n", "");

        jasperRestClient.put(url, requestBody);

        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq(url), eq(HttpMethod.PUT), captor.capture(), eq(Object.class));
        HttpEntity actualEntity = captor.getValue();
        assertEquals(requestBody, actualEntity.getBody());
        assertEquals(expectedAuthorisationHeader, actualEntity.getHeaders().get("Authorization").get(0));
    }
}
