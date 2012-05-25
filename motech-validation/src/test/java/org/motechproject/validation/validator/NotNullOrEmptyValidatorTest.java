package org.motechproject.validation.validator;

import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationValidationContext.xml")
public class NotNullOrEmptyValidatorTest {

    @Autowired
    private NotNullOrEmptyValidator notNullOrEmptyValidator;

    @Test
    public void shouldNotReportErrorWhenFieldIsNotNull_AndItsScopeIsNotSpecified() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("notNull", "someValue", "someValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        notNullOrEmptyValidator.validateField(target, ClassWithValidations.class.getDeclaredField("fieldWithoutScope"), "matchingScope", errors);
        assertEquals(0, errors.getFieldErrors().size());
    }

    @Test
    public void shouldReportErrorWhenFieldIsNull_AndItsScopeIsNotSpecified() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations(null, "someValue", "someValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        notNullOrEmptyValidator.validateField(target, ClassWithValidations.class.getDeclaredField("fieldWithoutScope"), "matchingScope", errors);
        assertEquals(1, errors.getFieldErrors().size());
        assertEquals("value should not be null", errors.getFieldErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldReportErrorWhenFieldIsNull_AndBothFieldAndValidationScopeIsNotSpecified() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations(null, null, null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        notNullOrEmptyValidator.validateField(target, ClassWithValidations.class.getDeclaredField("fieldWithoutScope"), null, errors);
        assertEquals(1, errors.getFieldErrors().size());
    }

    @Test
    public void shouldReportErrorWhenFieldIsNull_AndFieldScopeMatchesValidationScope() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("someValue", null, "someValue");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        notNullOrEmptyValidator.validateField(target, ClassWithValidations.class.getDeclaredField("fieldWithMatchingScope"), "matchingScope", errors);
        assertEquals(1, errors.getFieldErrors().size());
        assertEquals("value should not be null", errors.getFieldErrors().get(0).getDefaultMessage());
    }

    @Test
    public void shouldNotReportErrorWhenFieldIsNull_AndFieldScopeDoesNotMatchValidationScope() throws NoSuchFieldException {
        ClassWithValidations target = new ClassWithValidations("someValue", "someValue", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(target, "classWithValidations");
        notNullOrEmptyValidator.validateField(target, ClassWithValidations.class.getDeclaredField("fieldWithoutMatchingScope"), "someScope", errors);
        assertEquals(0, errors.getFieldErrors().size());
    }

    @Data
    public static class ClassWithValidations {

        @NotNullOrEmpty
        private String fieldWithoutScope;

        @NotNullOrEmpty(scope = "matchingScope")
        private String fieldWithMatchingScope;

        @NotNullOrEmpty(scope = "nonMatchingScope")
        private String fieldWithoutMatchingScope;

        public ClassWithValidations(String fieldWithoutScope, String fieldWithMatchingScope, String fieldWithoutMatchingScope) {
            this.fieldWithoutScope = fieldWithoutScope;
            this.fieldWithMatchingScope = fieldWithMatchingScope;
            this.fieldWithoutMatchingScope = fieldWithoutMatchingScope;
        }

    }

}
