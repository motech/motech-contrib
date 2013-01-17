package org.motechproject.jasper.reports.util;

import org.apache.commons.codec.binary.Base64;
import org.motechproject.jasper.reports.ReportsProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class JasperRESTClient {

    private RestTemplate restTemplate;
    private final ReportsProperties properties;

    public JasperRESTClient() {
        properties = new ReportsProperties();
        restTemplate = new RestTemplate();
    }

    public JasperRESTClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.properties = new ReportsProperties();
    }

    public void deleteResource(String resourceName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String authorisation = properties.getJasperServerUserName() + ":" + properties.getJasperServerPassword();
        byte[] encodedBytes = new Base64().encode(authorisation.getBytes());
        httpHeaders.add("Authorization", "Basic " + new String(encodedBytes).replace("\n", ""));
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(null, httpHeaders);
        restTemplate.exchange(properties.getJasperServerResourceURL() + resourceName, HttpMethod.DELETE, httpEntity, Object.class);
    }

    public void put(String url, Object requestBody) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String authorisation = properties.getJasperServerUserName() + ":" + properties.getJasperServerPassword();
        byte[] encodedBytes = new Base64().encode(authorisation.getBytes());
        httpHeaders.add("Authorization", "Basic " + new String(encodedBytes).replace("\n", ""));
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(requestBody, httpHeaders);
        restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Object.class);
    }
}
