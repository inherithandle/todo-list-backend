package com.gtchoi.todolistbackend.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VariableNamingPolicyValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface VariableNamingPolicy {
    String message() default "ID는 알파벳으로 시작해야하며, 특수문자는 _만 사용가능합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}