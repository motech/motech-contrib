package org.motechproject.web.filter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

class MotechHttpRequest extends HttpServletRequestWrapper {
    private StringBuilder stringBuilder;

    public MotechHttpRequest(HttpServletRequest request) {
        super(request);
        stringBuilder = new StringBuilder();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new TeeServletInputStream(super.getInputStream(), stringBuilder);
    }

    public String requestBody() {
        return stringBuilder.toString();
    }

}
