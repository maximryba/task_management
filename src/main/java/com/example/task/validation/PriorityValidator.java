package com.example.task.validation;

import com.example.task.model.Priority;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriorityValidator implements ConstraintValidator<ValidPriority, Priority> {

    @Override
    public boolean isValid(Priority value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value == Priority.LOW || value == Priority.MEDIUM || value == Priority.HIGH;
    }
}

