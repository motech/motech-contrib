package org.motechproject.http.client.service;

public interface HttpClientService {
    void post(String url, Object data);

    void put(String url, Object data);
}
