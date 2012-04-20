package org.ei.commcare.api.util;

import org.apache.http.Header;

import java.util.Arrays;

public class CommCareHttpResponse {
    private final int statusCode;
    private final Header[] headers;
    private final byte[] responseContent;

    public CommCareHttpResponse(int statusCode, Header[] headers, byte[] content) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.responseContent = content;
    }

    public String contentAsString() {
        return new String(responseContent);
    }

    private String header(String key) {
        for (Header header : headers) {
            if (header.getName().equals(key)) {
                return header.getValue();
            }
        }
        return "";
    }

    public String tokenForNextExport() {
        return header("X-CommCareHQ-Export-Token");
    }

    public boolean hasValidExportToken() {
        return statusCode == 200 && !tokenForNextExport().isEmpty();
    }

    @Override
    public String toString() {
        return "Status code: " + statusCode + ", Headers: " + Arrays.asList(headers) + ", Content: " + contentAsString();
    }

    public boolean isFailure() {
        return statusCode != 200 && statusCode != 302;
    }
}
