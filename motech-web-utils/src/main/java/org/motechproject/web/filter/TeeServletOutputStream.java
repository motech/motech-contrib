package org.motechproject.web.filter;

import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

public class TeeServletOutputStream extends ServletOutputStream {
    private final OutputStream targetStream;
    private StringWriter stringWriter;

    public TeeServletOutputStream(OutputStream targetStream, StringWriter stringWriter) {
        this.stringWriter = stringWriter;
        Assert.notNull(targetStream, "Target OutputStream must not be null");
        this.targetStream = targetStream;
    }

    public void write(int b) throws IOException {
        this.targetStream.write(b);
        stringWriter.write(b);
    }

    public void flush() throws IOException {
        super.flush();
        this.targetStream.flush();
    }

    public void close() throws IOException {
        super.close();
        this.targetStream.close();
    }
}
