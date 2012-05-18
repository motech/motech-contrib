package org.motechproject.importer;

import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@CSVImporter(entity = "sampleEntity", bean = SampleBean.class)
@Component
public class SampleCSVImporter {
    public boolean isPostCalled;
    public boolean isValidateCalled;
    public List<SampleBean> sampleBeans;

    @Validate
    public boolean validate(List<Object> objects) {
        isValidateCalled = true;
        return true;
    }

    @Post
    public void post(List<Object> objects) {
        isPostCalled = true;
        sampleBeans = new ArrayList<SampleBean>();
        for (Object object : objects) {
            sampleBeans.add((SampleBean) object);
        }
    }
}
