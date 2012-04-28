package org.motechproject.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.validation.validator.JSRConstraintsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;

import javax.validation.constraints.NotNull;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationValidationContext.xml")
public class JSRConstraintsValidatorTest {

    @Autowired
    JSRConstraintsValidator jsrConstraintsValidator;

    @Test
    public void shouldNotReportErrorWhenFieldIsNotNull() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("notNull", "someValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        jsrConstraintsValidator.validateField(target, ClassWithValidations.class.getDeclaredField("notNullField"), errors);

        assertEquals(0, errors.getFieldErrors().size());
    }

    @Test
    public void shouldReportErrorWhenFieldIsNull() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations(null, "someValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        jsrConstraintsValidator.validateField(target, ClassWithValidations.class.getDeclaredField("notNullField"), errors);

        assertEquals(1, errors.getFieldErrors().size());
        assertEquals("may not be null", errors.getFieldError("notNullField").getDefaultMessage());
    }

    @Test
    public void shouldNotValidateAFieldWithoutConstraintAnnotation() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("notNull", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        jsrConstraintsValidator.validateField(target, ClassWithValidations.class.getDeclaredField("fieldWithoutValidation"), errors);

        assertEquals(0, errors.getFieldErrors().size());
    }

    @Data
    public static class ClassWithValidations {

        @NotNull
        private String notNullField;

        private String fieldWithoutValidation;

        public ClassWithValidations(String notNullField, String fieldWithoutValidation) {
            this.notNullField = notNullField;
            this.fieldWithoutValidation = fieldWithoutValidation;
        }
    }

}
