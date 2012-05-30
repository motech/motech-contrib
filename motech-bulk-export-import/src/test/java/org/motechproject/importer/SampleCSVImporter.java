package org.motechproject.importer;

import org.motechproject.importer.annotation.CSVImporter;
import org.motechproject.importer.annotation.Post;
import org.motechproject.importer.annotation.Validate;
import org.motechproject.importer.domain.Error;
import org.motechproject.importer.domain.ValidationResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@CSVImporter(entity = "sampleEntity", bean = SampleBean.class)
@Component
public class SampleCSVImporter {
    public boolean isPostCalled;
    public boolean isValidateCalled;
    private boolean isValid;
    public List<SampleBean> sampleBeans;

    @Validate
    public ValidationResponse validate(List<Object> objects) {
        isValidateCalled = true;
        ValidationResponse validationResponse = new ValidationResponse(isValid);
        validationResponse.addError(new Error("this is a sample error"));
        return validationResponse;
    }

    @Post
    public void post(List<Object> objects) {
        isPostCalled = true;
        sampleBeans = new ArrayList<SampleBean>();
        for (Object object : objects) {
            sampleBeans.add((SampleBean) object);
        }
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
