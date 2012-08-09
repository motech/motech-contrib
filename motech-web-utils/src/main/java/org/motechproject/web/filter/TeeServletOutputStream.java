package org.motechproject.web.filter;

import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class TeeServletOutputStream extends ServletOutputStream {
    private final OutputStream targetStream;
    private StringBuilder stringBuilder;

    public TeeServletOutputStream(OutputStream targetStream, StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
        Assert.notNull(targetStream, "Target OutputStream must not be null");
        this.targetStream = targetStream;
    }

    public void write(int b) throws IOException {
        this.targetStream.write(b);
        stringBuilder.append((char) b);
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
