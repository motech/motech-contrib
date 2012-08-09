package org.motechproject.web.filter;

import org.springframework.util.Assert;

import javax.servlet.ServletInputStream;
import java.io.IOException;

class TeeServletInputStream extends ServletInputStream {

    private static final int END_OF_STREAM = -1;
    private ServletInputStream _servletInputStream;
    private StringBuilder stringBuilder;

    public TeeServletInputStream(ServletInputStream sourceInputStream, StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
        Assert.notNull(sourceInputStream, "Source InputStream must not be null");
        this._servletInputStream = sourceInputStream;
    }

    public int read() throws IOException {

        int character = _servletInputStream.read();
        if (character != END_OF_STREAM) {
            writeChar((char) character);
        }
        return character;
    }

    @Override
    public int read(byte[] bytes) throws IOException {

        int charactersRead = _servletInputStream.read(bytes);
        if (charactersRead != END_OF_STREAM) {
            for (byte character : bytes) {
                writeChar((char) character);
            }
        }
        return charactersRead;
    }

    @Override
    public int read(byte[] bytes, int i, int i1) throws IOException {

        int charactersRead = _servletInputStream.read(bytes, i, i1);
        if (charactersRead != END_OF_STREAM) {
            for (byte character : bytes) {
                writeChar((char) character);
            }
        }
        return charactersRead;
    }

    @Override
    public int readLine(byte[] bytes, int i, int i1) throws IOException {

        int charactersRead = _servletInputStream.readLine(bytes, i, i1);
        if (charactersRead != END_OF_STREAM) {
            for (byte character : bytes) {
                writeChar((char) character);
            }
        }
        return charactersRead;
    }

    @Override
    public void close() throws IOException {
        super.close();
        _servletInputStream.close();
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        _servletInputStream.reset();
        stringBuilder = new StringBuilder();
    }

    private void writeChar(char c) {
        if (c != '\u0000') {
            stringBuilder.append(c);
        }
    }
}
