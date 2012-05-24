package org.motechproject.validation.validator;

import org.joda.time.format.DateTimeFormatter;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.validator.root.PropertyValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;

@Component
public class DateFieldValidator extends PropertyValidator {

    @Override
    public void validateField(Object target, Field field, String scope, Errors errors) {
        if (field.isAnnotationPresent(DateTimeFormat.class)) {
            validateDateTimeFormat(target, field, errors);
        }
    }

    private void validateDateTimeFormat(Object target, Field field, Errors errors) {
        DateTimeFormat annotation = field.getAnnotation(DateTimeFormat.class);
        String expectedFormat = annotation.pattern();
        DateTimeFormatter localDateFormatter = org.joda.time.format.DateTimeFormat.forPattern(expectedFormat);
        field.setAccessible(true);
        try {
            if (field.get(target) != null){

                String dateTime = (String) field.get(target);
                String value = dateTime.trim();
                if(annotation.validateEmptyString() || !value.isEmpty())
                    localDateFormatter.parseLocalDate(dateTime);
            }
        } catch (IllegalAccessException e) {
            errors.rejectValue(field.getName(), "access-error", e.getStackTrace(), e.getMessage());
        } catch (IllegalArgumentException e) {
            errors.rejectValue(field.getName(), "datetime-format-error", e.getMessage());
        } catch (Exception e) {
            errors.rejectValue(field.getName(), "datetime-format-error", e.getMessage());
        }
    }
}
