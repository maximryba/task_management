package com.example.task.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriorityValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPriority {
    String message() default "Недопустимое значение приоритета. Разрешены: LOW, MEDIUM, HIGH.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

