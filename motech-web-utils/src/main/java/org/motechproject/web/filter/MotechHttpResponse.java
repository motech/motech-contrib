package org.motechproject.web.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.StringWriter;

public class MotechHttpResponse extends HttpServletResponseWrapper {
    private StringWriter stringWriter;

    public MotechHttpResponse(ServletResponse response) {
        super((HttpServletResponse) response);
        stringWriter = new StringWriter();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new TeeServletOutputStream(super.getOutputStream(), stringWriter);
    }

    public String responseBody() {
        return stringWriter.toString();
    }
}
