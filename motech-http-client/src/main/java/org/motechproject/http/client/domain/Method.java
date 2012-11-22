package org.motechproject.http.client.domain;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public enum Method {
    POST {
        @Override
        public void execute(RestTemplate restTemplate, String url, Object request, Map<String, String> headers) {
            HttpHeaders httpHeaders = getHeaders(headers);
            HttpEntity<Object> httpEntity = new HttpEntity<>(request, httpHeaders);
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, Object.class);
        }
    },

    PUT {
        @Override
        public void execute(RestTemplate restTemplate, String url, Object request, Map<String, String> headers) {
            HttpHeaders httpHeaders = getHeaders(headers);
            HttpEntity<Object> httpEntity = new HttpEntity<>(request, httpHeaders);
            restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Object.class);
        }
    };

    private static HttpHeaders getHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers == null) return httpHeaders;
        for (String header : headers.keySet()) {
            httpHeaders.add(header, headers.get(header));
        }
        return httpHeaders;
    }

    public abstract void execute(RestTemplate restTemplate, String url, Object request, Map<String, String> headers);
}
