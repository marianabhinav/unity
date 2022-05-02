package com.amcrest.unity.accounting.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidator
        implements ConstraintValidator<ValidEnumValue, String> {

    private List<String> acceptedValues;

    @Override
    public void initialize(ValidEnumValue constraintAnnotation) {
        acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        return acceptedValues.contains(value);
    }
}