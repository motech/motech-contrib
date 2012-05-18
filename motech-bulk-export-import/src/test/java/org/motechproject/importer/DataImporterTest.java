package org.motechproject.importer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationBulkImportContext.xml")
public class DataImporterTest {

    @Autowired
    private CSVDataImporter dataImporter;

    @Autowired
    SampleCSVImporter sampleCSVImporter;

    @Test
    public void shouldImportSampleBean() {
        dataImporter.importData("sampleEntity", "sample.csv");
        assertTrue(sampleCSVImporter.isPostCalled);
        assertTrue(sampleCSVImporter.isValidateCalled);
        assertThat(sampleCSVImporter.sampleBeans.get(0).getSampleX(), is("123"));
        assertThat(sampleCSVImporter.sampleBeans.get(0).getSampleY(),is("456"));
    }
}
