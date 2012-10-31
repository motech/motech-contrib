package org.motechproject.http.client.service;

import java.io.Serializable;

public interface HttpClientService {
    void put(String url, Serializable data);
    void post(String url, Serializable data);
}
