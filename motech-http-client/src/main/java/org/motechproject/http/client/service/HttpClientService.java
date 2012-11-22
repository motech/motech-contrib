package org.motechproject.http.client.service;

import java.util.Map;

public interface HttpClientService {
    void post(String url, Object data);

    void post(String Url, Object data, Map<String, String> headers);

    void put(String url, Object data);

    void put(String url, Object data, Map<String, String> headers);
}
