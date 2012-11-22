package org.motechproject.http.client.service;

import org.motechproject.http.client.domain.Method;

import java.util.Map;

public interface HttpClientService {
    void post(String url, Object data);

    void post(String url, Object data, Map<String, String> headers);

    void put(String url, Object data);

    void put(String url, Object data, Map<String, String> headers);

    void execute(String url, Object data, Method method);

    void execute(String url, Object data, Method method,  Map<String, String> headers);

    void executeSync(String url, Object data, Method method);

    void executeSync(String url, Object data, Method method, Map<String, String> headers);
}
