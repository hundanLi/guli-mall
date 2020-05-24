package com.hundanli.common.valid.validator;

import com.hundanli.common.valid.SetValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-15 11:00
 **/
public class IntSetConstraintValidator implements ConstraintValidator<SetValue, Integer> {

    private final Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(SetValue constraintAnnotation) {
        int[] values = constraintAnnotation.values();
        for (int value : values) {
            set.add(value);
        }
        set.add(null);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }

}
