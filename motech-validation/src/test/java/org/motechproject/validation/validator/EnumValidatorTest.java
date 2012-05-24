package org.motechproject.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.validation.constraints.Enumeration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationValidationContext.xml")
public class EnumValidatorTest {

    @Autowired
    EnumValidator enumValidator;

    @Test
    public void shouldReportErrorWhenValueIsNotEnumerated() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("notAnEnum", "enumValue1","enumValue2");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField1"), null, errors);

        assertEquals("The value should be one of : [enumValue1, enumValue2]", errors.getFieldError("enumField1").getDefaultMessage());
    }

    @Test
    public void shouldNotReportErrorWhenValueIsEnumerated() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("enumValue1", "enumValue1","enumValue2");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField1"), null, errors);

        assertNull(errors.getFieldError("enumField1"));
    }

    @Test
    public void shouldNotReportErrorWhenValueIsNull() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations(null, "enumValue1","enumValue1");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField1"), null, errors);

        assertNull(errors.getFieldError("enumField1"));
    }

    @Test
    public void validationShouldHandleSpaceAndBeCaseInsensitive() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations(String.format("   %s   ",Enum.enumValue1.name().toUpperCase()), "enumValue2","enumValue1");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField1"), null, errors);

        assertNull(errors.getFieldError("enumField1"));
    }
    @Test
    public void shouldNotValidateFieldWithoutEnumerationAnnotation() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("enumValue1","enumValue2", "notEnumeratedValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField1"), null, errors);

        assertNull(errors.getFieldError("notValidated"));
    }
    @Test
    public void shouldNotValidateEmptyStringOnlyIfThePropertyIsSetInAnnotation() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("","  ", "");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField1"), null, errors);
        assertNotNull(errors.getFieldError("enumField1"));

        errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("enumField2"), null, errors);
        assertNull(errors.getFieldError("enumField2"));

        errors = new BeanPropertyBindingResult(target, "classWithValidations");
        enumValidator.validateField(target, ClassWithValidations.class.getDeclaredField("notValidated"), null, errors);
        assertNull(errors.getFieldError("notValidated"));
    }

    @Data
    public static class ClassWithValidations {

        @Enumeration(type = Enum.class)
        private String enumField1;

        @Enumeration(type = Enum.class, validateEmptyString = false)
        private String enumField2;

        private String notValidated;

        public ClassWithValidations(String enumField1, String enumField2,String notValidated) {
            this.enumField1 = enumField1;
            this.enumField2 = enumField2;
            this.notValidated = notValidated;
        }
    }

    public enum Enum {
        enumValue1, enumValue2
    }

}
