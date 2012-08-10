package org.motechproject.web.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.StringWriter;

class MotechHttpResponse extends HttpServletResponseWrapper {
    private StringBuilder stringBuilder;
    private String _contentType;

    public MotechHttpResponse(ServletResponse response) {
        super((HttpServletResponse) response);
        stringBuilder = new StringBuilder();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new TeeServletOutputStream(super.getOutputStream(), stringBuilder);
    }

    public String responseBody() {
        return stringBuilder.toString();
    }

    public void setContentType(String contentType) {
        super.getResponse().setContentType(contentType);
        this._contentType = contentType;
    }

    public String getContentType() {
        String contentType = super.getContentType();
        if (contentType == null) {
            contentType = super.getResponse().getContentType();
        }
        if (contentType == null) {
            return _contentType;
        }
        return contentType;
    }
}
