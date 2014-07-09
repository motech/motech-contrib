package org.motechproject.http.client.service;

public interface HttpClientService {
    void put(String url, Object data, String username, String password);
    void post(String url, Object data, String username, String password);
}
