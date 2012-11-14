package org.ei.commcare.api.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
public class CommCareHttpClient {
    private DefaultHttpClient httpClient;
    private final ReentrantLock lock = new ReentrantLock();
    private static Logger logger = LoggerFactory.getLogger(CommCareHttpClient.class.toString());

    public CommCareHttpClient() {
        this.httpClient = new DefaultHttpClient();
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 60000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 60000);
    }

    public CommCareHttpResponse  get(String url, String commcareBaseUrl, String userName, String password) {
        logger.debug("Fetching URL: " + url + " with username: " + userName + " with commcareBaseUrl: " + commcareBaseUrl);

        CommCareHttpResponse commCareHttpResponse = null;

        lock.lock();
        try {
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(commcareBaseUrl, 443, "DJANGO", "digest"),
                    new UsernamePasswordCredentials(userName, password));

            HttpResponse response = httpClient.execute(new HttpGet(url));
            Header[] headers = response.getAllHeaders();

            commCareHttpResponse = failureResponse(response.getStatusLine().getStatusCode(), headers);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                commCareHttpResponse = new CommCareHttpResponse(response.getStatusLine().getStatusCode(), headers, IOUtils.toByteArray(entity.getContent()));
            }
        } catch (Exception e) {
            return failureResponse(404, new Header[] { new BasicHeader("Exception-Happened", e.getMessage())});
        } finally {
            lock.unlock();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Got response for URL: " + url + ": " + commCareHttpResponse);
        }

        return commCareHttpResponse;
    }

    private CommCareHttpResponse failureResponse(int statusCode, Header[] headers) {
        return new CommCareHttpResponse(statusCode, headers, new byte[0]);
    }
}
