package org.motechproject.export.writer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CSVWriterTest {
    @Mock
    private Writer writer;

    @Test
    public void shouldWriteCsvDataGivenDataToConvert() throws IOException {
        CSVWriter csvWriter = new CSVWriter();
        List data = new ArrayList();
        data.add(new ValidCsvEntity("a", "b"));

        csvWriter.writeCSVFromData(writer, data);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer, times(2)).write(captor.capture(), anyInt(), anyInt());
        List<String> csvText = captor.getAllValues();
        assertEquals("some,someOther" + System.lineSeparator(), csvText.get(0));
        assertEquals("a,b" + System.lineSeparator(), csvText.get(1));
    }
}
