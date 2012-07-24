package org.motechproject.http.client.service;

import org.springframework.stereotype.Component;

@Component
public interface HttpClientService {

    void post(String url, Object data);
}
