package org.motechproject.http.client.service;

public interface HttpClientService {
    void put(String url, Object data);
    void post(String url, Object data);
}
