package org.motechproject.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationValidationContext.xml")
public class DateFieldValidatorTest {

    @Autowired
    DateFieldValidator dateFieldValidator;

    @Test
    public void shouldNotReportErrorForValidDateFormat() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11/12/2007", "11/12/2007 10:30:30", "11/12/2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("date"), null, errors);
        assertNull(errors.getFieldError("date"));
    }

    @Test
    public void shouldReportErrorForInValidDateFormat() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11-12-2007", "11/12/2007 10:30:30", "11/12/2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("date"), null, errors);
        assertEquals(1, errors.getFieldErrors().size());
        assertEquals("Invalid format: \"11-12-2007\" is malformed at \"-12-2007\"", errors.getFieldErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldNotReportErrorForValidDateTimeFormat() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11/12/2007", "11/12/2007 10:30:30", "11/12/2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateTime"), null, errors);
        assertNull(errors.getFieldError("dateTime"));
    }

    @Test
    public void shouldReportErrorForInValidDateTimeFormat() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11-12-2007", "11/12/2007 10-30:30", "11/12/2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateTime"), null, errors);
        assertEquals(1, errors.getFieldErrors().size());
        assertEquals("Invalid format: \"11/12/2007 10-30:30\" is malformed at \"-30:30\"", errors.getFieldErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldNotReportErrorForNullFieldValues() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations(null, null, null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("date"), null, errors);
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateTime"), null, errors);
        assertNull(errors.getFieldError("date"));
        assertNull(errors.getFieldError("dateTime"));
    }

    @Test
    public void shouldNotValidateFieldWithoutDateTimeFormatAnnotation() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("11-12-2007", "11/12/2007 10-30:30", "11-12-2007");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateWithoutValidation"), null, errors);
        assertEquals(0, errors.getFieldErrors().size());
    }

    @Test
    public void shouldNotValidateEmptyStringOnlyIfPropertyIsSetInAnnotation() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("", "  ", "");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("date"), null, errors);
        assertEquals(1, errors.getFieldErrors().size());

        errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateTime"), null, errors);
        assertEquals(0, errors.getFieldErrors().size());

        errors = new BeanPropertyBindingResult(target, "classWithValidations");
        dateFieldValidator.validateField(target, ClassWithValidations.class.getDeclaredField("dateWithoutValidation"), null, errors);
        assertEquals(0, errors.getFieldErrors().size());
    }

    @Data
    public static class ClassWithValidations {

        @DateTimeFormat(pattern = "dd/MM/YYYY")
        private String date;

        @DateTimeFormat(validateEmptyString = false)
        private String dateTime;

        private String dateWithoutValidation;

        public ClassWithValidations(String date, String dateTime, String dateWithoutValidation) {
            this.date = date;
            this.dateTime = dateTime;
            this.dateWithoutValidation = dateWithoutValidation;
        }
    }
}
