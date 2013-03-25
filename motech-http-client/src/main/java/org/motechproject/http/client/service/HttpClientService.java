package org.motechproject.http.client.service;

import org.motechproject.http.client.domain.EventCallBack;

import java.io.Serializable;
import java.util.HashMap;

public interface HttpClientService {
    void put(String url, Serializable data);
    void post(String url, Serializable data);
    void post(String url, Serializable data, HashMap<String, String> headers);
    void post(String url, Serializable data, HashMap<String, String> headers, EventCallBack eventCallBack);
}
