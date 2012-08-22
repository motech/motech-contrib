package org.motechproject.http.client.service;

import org.motechproject.http.client.domain.Method;

public interface HttpClientService {
    void post(String url, Object data);

    void put(String url, Object data);

    void execute(String url, Object data, Method method);

    void executeSync(String url, Object data, Method method);
}
